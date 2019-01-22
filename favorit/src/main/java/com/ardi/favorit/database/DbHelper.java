package com.ardi.favorit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.ardi.favorit.database.DbContract.favColumns.TABLE_FILM;

public class DbHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "dbfilms";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String CREATE_TABLE_FILM = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL )",
            TABLE_FILM,
            DbContract.favColumns._ID,
            DbContract.favColumns.JUDUL,
            DbContract.favColumns.OVERVIEW,
            DbContract.favColumns.RILIS,
            DbContract.favColumns.RATING,
            DbContract.favColumns.POSTER,
            DbContract.favColumns.BACKDROP,
            DbContract.favColumns.VOTE);


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FILM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_FILM);
        onCreate(db);
    }
}
