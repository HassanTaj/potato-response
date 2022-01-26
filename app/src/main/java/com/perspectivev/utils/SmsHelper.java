package com.perspectivev.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
//import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.Toast;

import com.perspectivev.repository.DbConsts;

import java.util.ArrayList;
import java.util.List;

public class SmsHelper {
    private SmsManager _smsManager;
    private Context _activity;
    private SubscriptionManager _localSubscriptionManager;


    public SmsHelper(Context activity, SmsManager smsManager) {
        _smsManager = smsManager;
        _activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            _localSubscriptionManager = SubscriptionManager.from(activity);
        }
    }

    @SuppressLint("MissingPermission")
    public void sendSms(String selectedSim,String sender, String responseText) {
        try {
            ArrayList<String> msgParts = _smsManager.divideMessage(responseText);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                Log.d("SMS_INFO", _localSubscriptionManager.getActiveSubscriptionInfoCount() + "");
                if (_localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                    List localList = _localSubscriptionManager.getActiveSubscriptionInfoList();

                    SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);
                    SubscriptionInfo simInfo2 = (SubscriptionInfo) localList.get(1);
                    //SendSMS From SIM One
                    if (selectedSim.equals(DbConsts.SIM_1)) {
                        _smsManager.getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId())
                                .sendMultipartTextMessage(sender, null, msgParts, null, null);
//                                        Toast.makeText(context, "Sending Sms From Sim 1.", Toast.LENGTH_SHORT).show();
                    }

                    //SendSMS From SIM Two
                    if (selectedSim.equals(DbConsts.SIM_2)) {
                        _smsManager.getSmsManagerForSubscriptionId(simInfo2.getSubscriptionId())
                                .sendMultipartTextMessage(sender, null, msgParts, null, null);
//                                        Toast.makeText(context, "Sending Sms From Sim 2.", Toast.LENGTH_SHORT).show();
                    }
                }
                // in case phoen has only 1 sim
                if (_localSubscriptionManager.getActiveSubscriptionInfoCount() == 1) {
                    _smsManager.sendMultipartTextMessage(sender, null, msgParts, null, null);
//                                    Toast.makeText(context, "Sending Sms from Default Sim.", Toast.LENGTH_SHORT).show();
                }
            }
            // in casae phone has a different android version
            else {
                _smsManager.sendTextMessage(sender, null, responseText, null, null);
//                            Toast.makeText(context, "Sending Sms", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(_activity, "SMS Failed to Send, Please try again. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This function finds the senderNumber from contactbook and returs if it exists
     * TODO: this value can be stored in db as a feature if the user wants to reply to unknown numbers
     * @param senderNumber
     * @return if senderNumber exists or not
     */
    public boolean contactExists(String senderNumber) {
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(senderNumber));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = _activity.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }// contactExists

}
