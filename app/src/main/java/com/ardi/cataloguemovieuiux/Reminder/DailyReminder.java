package com.ardi.cataloguemovieuiux.Reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.ardi.cataloguemovieuiux.R;
import com.ardi.cataloguemovieuiux.main.MainActivity;

import java.util.Calendar;

public class DailyReminder extends BroadcastReceiver {
    public static final String EXTRA_MESSAGE_PREFERENCE = "message";
    public static final String EXTRA_TYPE_PREFERENCE = "type";
    public static final int NOTIF_ID = 501;
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "ardi channel";

    public DailyReminder(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotif(context, context.getResources().getString(R.string.daily_message), intent.getStringExtra(EXTRA_MESSAGE_PREFERENCE),NOTIF_ID);
    }

    private void sendNotif(Context context, String title, String descrip, int notifId){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntent(intent)
                .getPendingIntent(NOTIF_ID, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri ringTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, descrip)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(title)
                .setContentText(descrip)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(ringTone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            builder.setChannelId(CHANNEL_ID);
            notificationManager.createNotificationChannel(channel);
        }
        if (notificationManager != null){
            notificationManager.notify(notifId, builder.build());
        }
    }
    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public void setAlarm(Context context, String type, String time, String message){
        cancelAlarm(context);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        intent.putExtra(EXTRA_MESSAGE_PREFERENCE, message);
        intent.putExtra(EXTRA_TYPE_PREFERENCE, type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID, intent, 0);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
