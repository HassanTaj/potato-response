package com.perspectivev.services;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.perspectivev.entities.AppServiceOption;
import com.perspectivev.entities.ServiceLog;
import com.perspectivev.recievers.IncommingCallReceiver;
import com.perspectivev.recievers.SmsReciever;
import com.perspectivev.repository.DbConsts;
import com.perspectivev.repository.implimentations.AppServiceOptionRepo;
import com.perspectivev.repository.implimentations.ServiceLogRepo;
import com.perspectivev.potatoresponse.MainActivity;
import com.perspectivev.potatoresponse.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ResponderService extends Service {

    public enum Action {
        START("com.pserspectivev.services.SmsReceiveService.Action.START"),
        STOP("com.pserspectivev.services.SmsReceiveService.Action.STOP");
        private String name;
        Action(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        // A reverse lookup table
        private static final Map<String, Action> lookup = new HashMap<>();

        static {
            for (Action action : Action.values()) {
                lookup.put(action.getName(), action);
            }
        }

        public static Action get(String action) {
            return lookup.get(action);
        }
    }

    public static final String SHOULD_ACQUIRE_WAKE_LOCK_EXTRA = "com.pserspectivev.services.SmsReceiveService.should_acquire_wake_lock";
    private static final String NOTIFICATION_CHANNEL_ID = "com.pserspectivev.services.SmsReceiveService.notification_channel_ID.1";
    private static final String WAKE_LOCK_TAG = "com.pserspectivev.services.SmsReceiveService:wake_lock_tag";

    // wake lock, prevents the service from being stop when battery optimizations occur during Doze Mode.
    private PowerManager.WakeLock wakeLock;
    private boolean shouldAcquireWakeLock = true;

    BroadcastReceiver smsReciever;
    BroadcastReceiver callReciever;
    private ServiceLogRepo _serviceLogRepo;
    private AppServiceOptionRepo _appServiceRepo;
    private ServiceLog glog;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
        if (intent != null) {
            shouldAcquireWakeLock = intent.getBooleanExtra(SHOULD_ACQUIRE_WAKE_LOCK_EXTRA, false);
            String  action = intent.getAction();
            Action smsAction = Action.get(action);
            if(action!=null && smsAction!=null) {
                switch (smsAction) {
                    case START:
                        IntentFilter smsIntentFilter = new IntentFilter();
                        smsIntentFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION); //"android.provider.Telephony.SMS_RECEIVED"
                        smsReciever = new SmsReciever();
                        registerReceiver(smsReciever, smsIntentFilter);

                        IntentFilter callIntentFilter = new IntentFilter();
                        smsIntentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED); //"android.intent.action.PHONE_STATE");
                        callReciever = new IncommingCallReceiver();
                        registerReceiver(callReciever,callIntentFilter);

                        break;
                    case STOP:
                        // TODO: Update AppServiceOptionRepo.
                        // TODO: Update the switch in MainActivity,
                        //  Special Case: STOP called when MainActivity is still open and visible.
                        stopSelf();
                        // What else is better to return from here?
                        // Not like initializing (already initialized) things is doing good.
                        return Service.START_NOT_STICKY;
                }
            }
        }


        _serviceLogRepo = new ServiceLogRepo(this);
        ServiceLog log = _serviceLogRepo.getByServiceType(DbConsts.ServiceType.BG_SERVICE);
        if (log == null) {
            ServiceLog sl = new ServiceLog(0,
                    this.getClass().getSimpleName(),
                    DbConsts.ServiceType.BG_SERVICE,
                    DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()),
                    "");
            _serviceLogRepo.create(sl);
        } else {
            log.setServiceStartTime(DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
            _serviceLogRepo.update(log);
        }


        if (wakeLock == null && shouldAcquireWakeLock) {
            acquireWakeLock();
        }

        createNotificationChannel();
        startForeground(1, buildNotification());

        return Service.START_STICKY; //super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "Destroyed", Toast.LENGTH_SHORT).show();
        _serviceLogRepo = new ServiceLogRepo(this);
        _appServiceRepo = new AppServiceOptionRepo(this);
        AppServiceOption op = _appServiceRepo.getById(1);
        if (op != null) {
            op.setServiceActive(false);
            _appServiceRepo.update(op);
        }
        ServiceLog log = _serviceLogRepo.getByServiceType(DbConsts.ServiceType.BG_SERVICE);
        if (log != null) {
            log.setServiceEndTime(DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
            _serviceLogRepo.update(log);
        }

        unregisterReceiver(smsReciever);
        unregisterReceiver(callReciever);
        releaseWakeLock();
        stopForeground(true);
        stopSelf();

        super.onDestroy();
    }

    private void acquireWakeLock() {
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);

        // Ignore this IDE warning, we wish to run the service indefinitely.
        wakeLock.acquire();
    }

    private void releaseWakeLock() {
        if (wakeLock != null) {
            if (wakeLock.isHeld())
                wakeLock.release();
            wakeLock = null;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "SmsReceiver Service Notification Channel",
                    // TODO: Make this a string resource,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            // TODO: set options like Notification light, lock screen visibility etc.
            //  + Feature: User should be able to customize if he wants the notification on
            //    lock screen.

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private Notification buildNotification() {
        // Select Builder based on SDK version.
        NotificationCompat.Builder notificationBuilder =
                (
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                                ? new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                                : new NotificationCompat.Builder(this)
                );

        // Intent to launch MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent notificationTapPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Intent to Stop this service.
        Intent stopServiceIntent = new Intent(this, ResponderService.class);
        stopServiceIntent.setAction(Action.STOP.getName());
        PendingIntent stopActionPendingIntent =
                PendingIntent.getService(this, 0, stopServiceIntent, 0);

        // Building the action button in the notification.
        NotificationCompat.Action stopAction =
                new NotificationCompat.Action.Builder(
                        R.mipmap.ic_launcher_round,
                        getString(R.string.response_notification_stop_action),
                        stopActionPendingIntent)
                        .build();

        // Returning an instance of Notification.
        return notificationBuilder
                .addAction(stopAction)
                .setContentIntent(notificationTapPendingIntent)
                .setSmallIcon(R.mipmap.pg_v4_round)
                .setContentTitle(getString(R.string.response_notification_title))
                .setContentText(getString(R.string.response_notification_description))
                .build();
    }
}
