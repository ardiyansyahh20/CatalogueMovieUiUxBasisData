package com.ardi.cataloguemovieuiux.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.BACKDROP;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.JUDUL;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.OVERVIEW;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.POSTER;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.RATING;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.RILIS;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.VOTE;
import static com.ardi.cataloguemovieuiux.Database.DbContract.getColumnInt;
import static com.ardi.cataloguemovieuiux.Database.DbContract.getColumnString;


public class MovieItems implements Parcelable {
    private int id;
    private String titleFilm;
    private String voteFilm;
    private String ratingFilm;
    private String rilisFilm;
    private String overviewFilm;
    private String imageFilm;
    private String backdropFilm;

    public MovieItems(JSONObject object) {
        try {
            int id = object.getInt("id");
            String title = object.getString("title");
            String vote = object.getString("vote_count");
            String rating = object.getString("vote_average");
            String releaseDate = object.getString("release_date");
            String overview = object.getString("overview");
            String image = object.getString("poster_path");
            String backdrop = object.getString("backdrop_path");

            this.id = id;
            this.titleFilm = title;
            this.voteFilm = vote;
            this.ratingFilm = rating;
            this.rilisFilm = releaseDate;
            this.overviewFilm = overview;
            this.imageFilm = image;
            this.backdropFilm = backdrop;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MovieItems() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdropFilm() {
        return backdropFilm;
    }

    public void setBackdropFilm(String backdropFilm) {
        this.backdropFilm = backdropFilm;
    }

    public String getTitleFilm() {
        return titleFilm;
    }

    public void setTitleFilm(String titleFilm) {
        this.titleFilm = titleFilm;
    }

    public String getVoteFilm() {
        return voteFilm;
    }

    public void setVoteFilm(String voteFilm) {
        this.voteFilm = voteFilm;
    }

    public String getRatingFilm() {
        return ratingFilm;
    }

    public void setRatingFilm(String ratingFilm) {
        this.ratingFilm = ratingFilm;
    }

    public String getRilisFilm() {
        return rilisFilm;
    }

    public void setRilisFilm(String rilisFilm) {
        this.rilisFilm = rilisFilm;
    }

    public String getOverviewFilm() {
        return overviewFilm;
    }

    public void setOverviewFilm(String overviewFilm) {
        this.overviewFilm = overviewFilm;
    }

    public String getImageFilm() {
        return imageFilm;
    }

    public void setImageFilm(String imageFilm) {
        this.imageFilm = imageFilm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.titleFilm);
        dest.writeString(this.voteFilm);
        dest.writeString(this.ratingFilm);
        dest.writeString(this.rilisFilm);
        dest.writeString(this.overviewFilm);
        dest.writeString(this.imageFilm);
        dest.writeString(this.backdropFilm);
    }

    protected MovieItems(Parcel in) {
        this.id = in.readInt();
        this.titleFilm = in.readString();
        this.voteFilm = in.readString();
        this.ratingFilm = in.readString();
        this.rilisFilm = in.readString();
        this.overviewFilm = in.readString();
        this.imageFilm = in.readString();
        this.backdropFilm = in.readString();
    }

    public MovieItems(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.titleFilm = getColumnString(cursor, JUDUL);
        this.overviewFilm = getColumnString(cursor, OVERVIEW);
        this.rilisFilm = getColumnString(cursor, RILIS);
        this.ratingFilm = getColumnString(cursor, RATING);
        this.imageFilm = getColumnString(cursor, POSTER);
        this.backdropFilm = getColumnString(cursor, BACKDROP);
        this.voteFilm = getColumnString(cursor, VOTE);

    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
}