package com.perspectivev.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.params.MandatoryStreamCombination;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class AppUtil {
    private Context _context;

    public AppUtil(Context context) {
        this._context = context;
    }

    /**
     * Check if the service is Running
     *
     * @param serviceClass the class of the Service
     * @return true if the service is running otherwise false
     */
    public boolean checkServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) _context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void getPermissions() {
        ArrayList<String> appPermissions = new ArrayList<>(Arrays.asList(
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.PROCESS_OUTGOING_CALLS
        ));

        ArrayList<String> permissionsToAsk = new ArrayList<>();
        for (String p : appPermissions) {
            String res = PermissionCheckReturn(_context, p);
            if (res != null && !res.equals("")) {
                permissionsToAsk.add(p);
            }
        }

        String[] arr = permissionsToAsk.toArray(new String[]{});
        if (arr.length > 0) {
            ActivityCompat.requestPermissions((Activity)_context, arr, 326);
        }
    }

    private String PermissionCheckReturn(Context ctx, String permission) {
        String res = null;
        if (ContextCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) ctx, permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                res = permission;
            }
        }
        return res;
    }
}
