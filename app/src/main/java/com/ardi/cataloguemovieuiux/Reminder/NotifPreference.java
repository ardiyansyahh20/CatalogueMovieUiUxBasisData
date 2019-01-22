package com.ardi.cataloguemovieuiux.Reminder;

import android.content.Context;
import android.content.SharedPreferences;

public class NotifPreference {
    public final static String PREFERENCE_NAME = "reminderPreferences";
    public final static String DAILY_REMINDER = "DailyReminder";
    public final static String REMINDER_MESSAGE_RELEASE = "reminderMessageRelease";
    public final static String REMINDER_MESSAGE_DAILY = "reminderMessageDaily";

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public NotifPreference(Context context){
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setDailyReminder(String time){
        editor.putString(DAILY_REMINDER, time);
        editor.commit();
    }

    public void setReminderMessageDaily(String message){
        editor.putString(REMINDER_MESSAGE_DAILY, message);
    }

    public void setReminderRelease(String time){
        editor.putString(DAILY_REMINDER, time);
        editor.commit();
    }

    public void setReminderMessageRelease(String message){
        editor.putString(REMINDER_MESSAGE_RELEASE, message);
    }
}
