package com.ardi.favorit.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.ardi.favorit.database.DbContract.favColumns.TABLE_FILM;

public class DbContract {

    public static final class favColumns implements BaseColumns {
        public static String TABLE_FILM = "favorite_film";

        public static String JUDUL = "judul";
        public static String OVERVIEW = "overview";
        public static String RILIS = "release";
        public static String RATING = "rating";
        public static String POSTER = "poster";
        public static String BACKDROP = "backdrop";
        public static String VOTE = "vote";

    }

    public static final String AUTHORITY = "com.ardi.cataloguemovieuiux";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_FILM)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}
