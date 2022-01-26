package com.perspectivev.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.perspectivev.entities.AppServiceOption;
import com.perspectivev.repository.implimentations.AppServiceOptionRepo;
import com.perspectivev.utils.SmsHelper;

import java.util.Calendar;

public class IncommingCallReceiver extends BroadcastReceiver {
    private static boolean ring = false;
    private static boolean callReceived = false;
    private static String _callerPhoneNumber = "";
    private Context _ctx;
    private SmsHelper _sh;
    private AppServiceOptionRepo _appServiceRepo;

    @Override
    public void onReceive(Context context, Intent intent) {
        _ctx = context;
        _appServiceRepo = new AppServiceOptionRepo(context.getApplicationContext());
        _sh = new SmsHelper(_ctx, SmsManager.getDefault());

//        ProcessCall(_ctx, intent);
        ProcessCall();
    }

    private void ProcessCall(Context context, Intent intent) {
        // Get the current Phone State
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        long currentTimeStamp = calendar.getTimeInMillis();
        // If phone state "Rininging"
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Bundle bundle = intent.getExtras();
//            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String number = bundle.getString("incoming_number");
            if (number != null && !number.equals("")) {
                _callerPhoneNumber = number;
            }
            ring = true;
            // Get the Caller's Phone Number
        }


        // If incoming call is received
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            callReceived = true;
        }


        // If phone is Idle
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
            if (ring == true && callReceived == false) {
                AppServiceOption aso = _appServiceRepo.getById(1);
                if (aso.isServiceActive() == true) {
                    if (_sh.contactExists(_callerPhoneNumber)) {
//                        Log.d("CALL_INFO", "\nContact Exists > " + _callerPhoneNumber);
                        _sh.sendSms(aso.getSelectedSim(), _callerPhoneNumber, aso.getResponseText());
//                        Toast.makeText(context, "missed call : " + _callerPhoneNumber, Toast.LENGTH_LONG).show();
                    }
                }
                ring = false;
            }
            callReceived = false;
        }
    }

    private void ProcessCall() {
        TelephonyManager telephony = (TelephonyManager) _ctx.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);

                // ringing
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // Get the Caller's Phone Number
                    if (incomingNumber != null && !incomingNumber.equals("")) {
                        _callerPhoneNumber = incomingNumber;
                    }
                    ring = true;
                }

                // If incoming call is received
                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    callReceived = true;
                }

                // If It's a Missed call
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
                    if (ring == true && callReceived == false) {
                        AppServiceOption aso = _appServiceRepo.getFirst();//.getById(1);
                        if (aso.isServiceActive()) {
                            if (_sh.contactExists(_callerPhoneNumber)) {
                                _sh.sendSms(aso.getSelectedSim(), _callerPhoneNumber, aso.getResponseText());
                                Log.d("CALL_INFO", _callerPhoneNumber);
                                Toast.makeText(_ctx, "incomingNumber : " + _callerPhoneNumber, Toast.LENGTH_LONG).show();
                            }
                        }
                        ring = false;
                    }
                    callReceived = false;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

}
