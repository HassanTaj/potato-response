package com.perspectivev.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.perspectivev.entities.AppServiceOption;
import com.perspectivev.repository.implimentations.AppServiceOptionRepo;
import com.perspectivev.utils.SmsHelper;


public class SmsReciever extends BroadcastReceiver {
    private AppServiceOptionRepo _appServiceRepo;
    private Context _ctx;
    private SmsHelper _sh;

    @Override
    public void onReceive(Context context, Intent intent) {
        _ctx = context;
        _appServiceRepo = new AppServiceOptionRepo(context.getApplicationContext());
        _sh = new SmsHelper(_ctx,SmsManager.getDefault());
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            AppServiceOption aso = _appServiceRepo.getFirst();//getById(1);
            if (aso.isServiceActive() == true) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    String messageBody = smsMessage.getMessageBody();
                    String sender = smsMessage.getDisplayOriginatingAddress();

                    if (_sh.contactExists(sender)) {
                        Log.d("SMS_INFO",
                                "\nContact Exists > " + sender);

                        Log.d("SMS_INFO",
                                "\nSender > " + sender +
                                        "\nMessage > " + messageBody +
                                        "\nGonna reply with > " + aso.getResponseText());
                        _sh.sendSms(aso.getSelectedSim(),sender,aso.getResponseText());
                    }
                }
//                Toast.makeText(context, "Service Is Active so i should send a message back", Toast.LENGTH_LONG).show();
            }
//            else {
//                Toast.makeText(context, "service is off", Toast.LENGTH_LONG).show();
//            }
        }

    }

}
