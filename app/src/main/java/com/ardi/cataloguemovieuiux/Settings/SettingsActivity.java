package com.ardi.cataloguemovieuiux.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.ardi.cataloguemovieuiux.R;
import com.ardi.cataloguemovieuiux.Reminder.DailyReminder;
import com.ardi.cataloguemovieuiux.Reminder.NotifPreference;
import com.ardi.cataloguemovieuiux.Reminder.ReleaseReminder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    public static final String TYPE_REMINDER_RECEIVE = "reminderAlarmRelease";
    public static final String TYPE_REMINDER_PREFERENCE = "reminderAlarm";
    public static final String REMINDER_UPCOMING = "upcomingReminder";
    public static final String REMINDER_DAILY = "dailyReminder";
    public static final String CHECKED_UPCOMING = "checkedUpcoming";
    public static final String CHECKED_DAILY = "checkedDaily";

    @BindView(R.id.daily_reminder)
    Switch dailyReminder;
    @BindView(R.id.release_reminder)
    Switch releaseReminder;

    public DailyReminder dailyReceive;
    public ReleaseReminder releaseReceive;
    public NotifPreference notifPreference;
    public SharedPreferences sDailyReminder, sReleaseReminder;
    public SharedPreferences.Editor editorDaily, editorRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        dailyReceive = new DailyReminder();
        releaseReceive = new ReleaseReminder();
        notifPreference = new NotifPreference(this);
        setPreference();
    }

    private void dailyOn() {
        String time = "19:05";
        String message = getResources().getString(R.string.daily_reminder);
        notifPreference.setDailyReminder(time);
        notifPreference.setReminderMessageDaily(message);
        dailyReceive.setAlarm(SettingsActivity.this, TYPE_REMINDER_RECEIVE, time, message);
    }

    private void dailyOff() {
        dailyReceive.cancelAlarm(SettingsActivity.this);
    }

    private void releaseOn() {
        String time = "08:00";
        String message = getResources().getString(R.string.release_movie);
        notifPreference.setReminderRelease(time);
        notifPreference.setReminderMessageRelease(message);
        releaseReceive.setAlarm(SettingsActivity.this, TYPE_REMINDER_PREFERENCE, time, message);
    }

    private void releaseOff() {
        releaseReceive.cancelAlarm(SettingsActivity.this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setPreference() {
        sDailyReminder = getSharedPreferences(REMINDER_DAILY, MODE_PRIVATE);
        sReleaseReminder = getSharedPreferences(REMINDER_UPCOMING, MODE_PRIVATE);
        boolean checkDaily = sReleaseReminder.getBoolean(CHECKED_DAILY, false);
        dailyReminder.setChecked(checkDaily);
        boolean checkUpcoming = sReleaseReminder.getBoolean(CHECKED_UPCOMING, false);
        releaseReminder.setChecked(checkUpcoming);
    }

    @OnCheckedChanged(R.id.daily_reminder)
    public void setDailyReminder(boolean isChecked){
        editorDaily = sDailyReminder.edit();
        if (isChecked){
            editorDaily.putBoolean(CHECKED_DAILY, true);
            editorDaily.commit();
            dailyOn();
        }else {
            editorDaily.putBoolean(CHECKED_DAILY, false);
            editorDaily.commit();
            dailyOff();
        }
    }

    @OnCheckedChanged(R.id.release_reminder)
    public void setReleaseReminder(boolean isChecked){
        editorRelease = sReleaseReminder.edit();
        if (isChecked){
            editorRelease.putBoolean(CHECKED_UPCOMING, true);
            editorRelease.commit();
            releaseOn();
        }else {
            editorRelease.putBoolean(CHECKED_UPCOMING, false);
            editorRelease.commit();
            releaseOff();
        }
    }

    @OnClick(R.id.change_lang)
    public void onViewClicked(View view){
        Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(intent);
    }
}
