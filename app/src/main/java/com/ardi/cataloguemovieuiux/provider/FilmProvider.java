package com.ardi.cataloguemovieuiux.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ardi.cataloguemovieuiux.Database.FilmHelper;

import static com.ardi.cataloguemovieuiux.Database.DbContract.AUTHORITY;
import static com.ardi.cataloguemovieuiux.Database.DbContract.CONTENT_URI;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.TABLE_FILM;

public class FilmProvider extends ContentProvider {
    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    private static final UriMatcher sUrimatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUrimatcher.addURI(AUTHORITY, TABLE_FILM, MOVIE);
        sUrimatcher.addURI(AUTHORITY, TABLE_FILM + "/#", MOVIE_ID);
    }

    private FilmHelper filmHelper;

    @Override
    public boolean onCreate() {
        filmHelper = new FilmHelper(getContext());
        filmHelper.open();
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUrimatcher.match(uri)) {
            case MOVIE:
                cursor = filmHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = filmHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long added;
        switch (sUrimatcher.match(uri)) {
            case MOVIE:
                added = filmHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUrimatcher.match(uri)) {
            case MOVIE_ID:
                deleted = filmHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updated;
        switch (sUrimatcher.match(uri)) {
            case MOVIE_ID:
                updated = filmHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }
}
