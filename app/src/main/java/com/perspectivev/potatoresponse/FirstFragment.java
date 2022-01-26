package com.perspectivev.potatoresponse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.perspectivev.entities.AppServiceOption;
import com.perspectivev.entities.ServiceLog;
import com.perspectivev.repository.DbConsts;
import com.perspectivev.repository.implimentations.AppServiceOptionRepo;
import com.perspectivev.repository.implimentations.ServiceLogRepo;
import com.perspectivev.services.ResponderService;
import com.perspectivev.utils.AppUtil;

public class FirstFragment extends Fragment {
    private Context _context;
    private Activity _activity;
    private AppServiceOptionRepo _appServiceOptions;
    private ServiceLogRepo _serviceLogRepo;
    private AppUtil _au;
    private RadioGroup simRadioGroup;
    private RadioButton simRadioButton;
    private Switch activeStatusSwitch;
    EditText et;
    TextView logTextView;
    String logText = "No Logs Available";
    ServiceLog log = null;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        this._context = inflater.getContext().getApplicationContext();
        this._activity = getActivity();
        this._au = new AppUtil(getActivity());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        et = v.findViewById(R.id.responseText);
        activeStatusSwitch = v.findViewById(R.id.serviceActiveStatus);
        simRadioGroup = v.findViewById(R.id.simSelectRadioGroup);
        _appServiceOptions = new AppServiceOptionRepo(_context);
        _serviceLogRepo = new ServiceLogRepo(_context);

        log = _serviceLogRepo.getByServiceType(DbConsts.ServiceType.BG_SERVICE);
        if (log != null) {
            OnScreenLog(logTextView, log.toString());
        }

        if (_appServiceOptions.getAll().size() == 0) {
            AppServiceOption appServiceOption = new AppServiceOption();
            appServiceOption.setServiceActive(false);
            appServiceOption.setResponseText("Hi there lets talk later.");
            appServiceOption.setSelectedSim(DbConsts.SIM_1);
            _appServiceOptions.create(appServiceOption);
            et.setText(appServiceOption.getResponseText());
            activeStatusSwitch.setChecked(appServiceOption.isServiceActive());
            simRadioGroup.check(appServiceOption.getSelectedSim().equals(DbConsts.SIM_1) ? R.id.sim1Radio : R.id.sim2Radio);
            Log.d("SIM_INFO", "selected sim : " + appServiceOption.getSelectedSim());

        } else {
            AppServiceOption aso = _appServiceOptions.getFirst();//getById(1);
            if (aso != null) {
            et.setText(aso.getResponseText());
            activeStatusSwitch.setChecked(aso.isServiceActive());
            Log.d("SIM_INFO", "selected sim : " + aso.getSelectedSim());
            simRadioGroup.check((aso.getSelectedSim().equals(DbConsts.SIM_1)) ? R.id.sim1Radio : R.id.sim2Radio);
            }
        }


        // save message to respond with
        v.findViewById(R.id.saveResponseTextBtn).setOnClickListener(view -> {
            AppServiceOption opt = _appServiceOptions.getFirst();//getById(1);
            opt.setResponseText(et.getText().toString());
            _appServiceOptions.update(opt);
            Snackbar.make(view, "Message Saved", Snackbar.LENGTH_LONG).show();
        });

        // swith to determin if service is on or off
        activeStatusSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            AppServiceOption opt = _appServiceOptions.getFirst();//getById(1);
            opt.setServiceActive(b);
            _appServiceOptions.update(opt);
            if (opt.isServiceActive()) {

                Intent responderService = new Intent(_activity, ResponderService.class);
                responderService.setAction(ResponderService.Action.START.getName());
                responderService.putExtra(ResponderService.SHOULD_ACQUIRE_WAKE_LOCK_EXTRA, false);

                // where the serve is run finally
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    _activity.startForegroundService(responderService);
//                        startForegroundService(incomingCallIntent);
                } else {
                    _activity.startService(responderService);
//                        startService(incomingCallIntent);
                }
            } else {

                Intent serviceIntent = new Intent(_context, ResponderService.class);
                if (_au.checkServiceRunning(ResponderService.class)) {
                    _activity.stopService(serviceIntent);
//                        stopService(incomingCallIntent);
                }
            }

            log = _serviceLogRepo.getByServiceType(DbConsts.ServiceType.BG_SERVICE);
            if (log != null) {
                OnScreenLog(logTextView, log.toString());
            }
        });

        // sim radio
        simRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            simRadioButton = v.findViewById(radioGroup.getCheckedRadioButtonId());
            AppServiceOption aso = _appServiceOptions.getFirst(); //getById(1);
            aso.setSelectedSim(simRadioButton.getText().equals(DbConsts.SIM_1) ? DbConsts.SIM_1 : DbConsts.SIM_2);
            Log.d("SIM_INFO", "selected sim : " + aso.getSelectedSim());
            _appServiceOptions.update(aso);
//                Toast.makeText(context, "id: " + simRadioButton.getText(), Toast.LENGTH_SHORT).show();
        });

    }

    private void OnScreenLog(TextView logTextView, String s) {
//        logTextView.setText(s);
    }
}