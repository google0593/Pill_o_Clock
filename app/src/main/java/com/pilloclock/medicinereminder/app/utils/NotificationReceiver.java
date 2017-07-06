package com.pilloclock.medicinereminder.app.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pilloclock.medicinereminder.app.activities.LoginActivity;
import com.pilloclock.medicinereminder.app.R;

import java.util.Calendar;

import static io.fabric.sdk.android.Fabric.TAG;

/***
 * Created by nikol on May.
 * Project name: Pillo'Clock
 */

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentToStartWhenAlarmSets = new Intent(context, LoginActivity.class);
        intentToStartWhenAlarmSets.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToStartWhenAlarmSets, 0);

        String _text = "Please take " + intent.getStringExtra("amount") + " of " + intent.getStringExtra("medicine");
        int sickEmoji = 0x1F912;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_stat_time_for_medicine)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setContentTitle("Time to take your meds! " + getEmoji(sickEmoji))
                .setContentText(_text)
                .setDefaults(Notification.DEFAULT_ALL);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());

        Log.d(TAG, "onReceive INTENT: " + "Please take " + intent.getStringExtra("amount") + " of " + intent.getStringExtra("medicine"));
        Log.d(TAG, "life cycle check: onReceive");
    }

    //init
    private Intent _intent;
    private int _interval;

    private String getEmoji(int sickEmoji){
        return new String(Character.toChars(sickEmoji));
    }

    public void setNotifData(Context context, String medicine, String interval,String amount) {
        //initialize intent
        _intent = new Intent(context, NotificationReceiver.class);
        _intent.putExtra("medicine", medicine);
        _intent.putExtra("interval", interval);
        _intent.putExtra("amount", amount);
        _interval = Integer.parseInt(_intent.getStringExtra("interval"));
    }

    public void setAlarm(Context context, int hour, int min, int notifyID) {
        Log.d(TAG, "setAlarm: notify ID " + notifyID);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        Log.d(TAG, "setAlarm: " + hour + ":" + min);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
            Log.d(TAG, "setAlarm: PAST SO + 1 DAY");
        } else {
            Log.d(TAG, "setAlarm: FUTURE DON'T DO ANYTHING");
        }

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pi = PendingIntent.getBroadcast(context, notifyID, _intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //params alarm type, when to start, interval, intent
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * (_interval * 3600), pi);

        Log.d(TAG, "setAlarm: calendarMills" + calendar.getTimeInMillis() +  " Interval: " +_interval);
        Log.d(TAG, "life cycle check: set alarm ");

    }

    public void cancelNotification(Context context, int notifyID) {
        Intent intentCancel = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(context, notifyID, intentCancel, 0);
        AlarmManager alarmManagerCancel = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManagerCancel != null) {
            alarmManagerCancel.cancel(pendingIntentCancel);
            Log.d(TAG, "cancelNotification: notification canceled");
        } else {
            Log.d(TAG, "cancelNotification: nothing to cancel");
        }
    }

}
