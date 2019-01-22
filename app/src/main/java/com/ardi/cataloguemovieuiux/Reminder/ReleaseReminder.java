package com.ardi.cataloguemovieuiux.Reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ardi.cataloguemovieuiux.BuildConfig;
import com.ardi.cataloguemovieuiux.R;
import com.ardi.cataloguemovieuiux.entity.MovieItems;
import com.ardi.cataloguemovieuiux.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReleaseReminder extends BroadcastReceiver {

    public static final String EXTRA_MESSAGE_RECEIVE = "messageRelease";
    public static final String EXTRA_TYPE_RECEIVE = "typeRelease";
    private static final String API_URL = BuildConfig.MOVIE_URL + "/upcoming?api_key=" + BuildConfig.MOVIE_API_KEY + "&language=en-US";
    private static int NOTIF_ID = 502;
    public static String CHANNEL_ID = "channel_02";
    public static CharSequence CHANNEL_NAME = "ardi channel";

    private ArrayList<MovieItems> movieItemsList = new ArrayList<>();


    @Override
    public void onReceive(final Context context, Intent intent) {
        getReleaseAlarm(context);
    }

    public void sendNotif(Context context, String title, String message, int id) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setSound(ringtone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notif.setChannelId(CHANNEL_ID);
            notificationManager.createNotificationChannel(channel);
        }
        if (notificationManager != null) {
            notificationManager.notify(id, notif.build());
        }
    }

    private void getReleaseAlarm(final Context context) {
        StringRequest request = new StringRequest(Request.Method.GET, API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject film = array.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(film);
                        movieItems.setTitleFilm(film.getString("title"));
                        movieItems.setRilisFilm(film.getString("release_date"));
                        movieItemsList.add(movieItems);
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = new Date();
                    final String now = dateFormat.format(date);
                    for (int i = 0; i < movieItemsList.size(); i++) {
                        MovieItems items = movieItemsList.get(i);
                        if (items.getRilisFilm().equals(now)) {
                            String judul = "Release Today";
                            String message = items.getTitleFilm();
                            sendNotif(context, judul, message, NOTIF_ID);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                getReleaseAlarm(context);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, R.string.movie_daily_reminder, Toast.LENGTH_SHORT).show();
    }

    public void setAlarm (Context context, String type, String time, String message){
        cancelAlarm(context);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        intent.putExtra(EXTRA_MESSAGE_RECEIVE, message);
        intent.putExtra(EXTRA_TYPE_RECEIVE, type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID, intent, 0);
        if (alarm != null){
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, R.string.movie_release_reminder, Toast.LENGTH_SHORT).show();

    }


}
