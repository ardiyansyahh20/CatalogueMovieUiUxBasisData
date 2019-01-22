package com.ardi.cataloguemovieuiux.search;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.ardi.cataloguemovieuiux.Database.FilmHelper;
import com.ardi.cataloguemovieuiux.R;
import com.ardi.cataloguemovieuiux.entity.MovieItems;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;
import static com.ardi.cataloguemovieuiux.BuildConfig.BACKDROP_URL;
import static com.ardi.cataloguemovieuiux.BuildConfig.POSTER_URL;
import static com.ardi.cataloguemovieuiux.Database.DbContract.CONTENT_URI;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.BACKDROP;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.JUDUL;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.OVERVIEW;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.POSTER;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.RATING;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.RILIS;
import static com.ardi.cataloguemovieuiux.Database.DbContract.favColumns.VOTE;

public class DetailFilmActivity extends AppCompatActivity {


    @BindView(R.id.tv_detail_film)
    TextView tvJudul;
    @BindView(R.id.tv_detail_item_overview)
    TextView tvOverview;
    @BindView(R.id.tv_rilis_film)
    TextView tvRelease;
    @BindView(R.id.poster_film)
    ImageView imageMovie;
    @BindView(R.id.image_detail)
    ImageView backdropMovie;
    @BindView(R.id.tv_rating_film)
    TextView tvRating;
    @BindView(R.id.tv_vote_film)
    TextView tvVote;
    @BindView(R.id.fab)
    FloatingActionButton fabFav;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collaps;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FilmHelper helper;
    private Boolean isFavorite = false;
    private MovieItems film;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        ButterKnife.bind(this);
//        helper = new FilmHelper(this);

//        ContentValues values = new ContentValues();
//        values.put(BACKDROP, film.getBackdropFilm());
//        values.put(JUDUL_FILM, film.getTitleFilm());
//        values.put(OVERVIEW, film.getOverviewFilm());
//        values.put(RATING, film.getRatingFilm());
//        values.put(VOTE, film.getVoteFilm());
//        values.put(POSTER, film.getImageFilm());
//        values.put(RILIS, film.getRilisFilm());
//        helper.open();
//        helper.insertProvider(values);
        film = getIntent().getParcelableExtra("film");

        setFilm();
        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    FavoriteRemove();
                } else {
                    FavoriteSave();
                }
                isFavorite = !isFavorite;
                favoriteSet();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.close();
        }
    }

    private void favoriteSet() {
        if (isFavorite) {
            fabFav.setImageResource(R.drawable.ic_favorite);
        } else {
            fabFav.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void FavoriteSave() {
        ContentValues values = new ContentValues();
        values.put(_ID, film.getId());
        values.put(JUDUL, film.getTitleFilm());
        values.put(OVERVIEW, film.getOverviewFilm());
        values.put(RILIS, film.getRilisFilm());
        values.put(RATING, film.getRatingFilm());
        values.put(POSTER, film.getImageFilm());
        values.put(BACKDROP, film.getBackdropFilm());
        values.put(VOTE, film.getVoteFilm());
//        helper.open();
        getContentResolver().insert(CONTENT_URI, values);
        Toast.makeText(this, getString(R.string.addFav), Toast.LENGTH_SHORT).show();
    }

    private void FavoriteRemove() {
        getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + film.getId()), null, null);
        Toast.makeText(this, getString(R.string.delFav), Toast.LENGTH_SHORT).show();
    }


    private void setFilm() {
        loadDatabase();

        collaps.setTitle(film.getTitleFilm());
        tvJudul.setText(film.getTitleFilm());
        tvOverview.setText(film.getOverviewFilm());
        tvRating.setText(getString(R.string.rating, film.getRatingFilm()));
        tvVote.setText(getString(R.string.vote, film.getVoteFilm()));
        loadImage(film);
        String release_date = film.getRilisFilm();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = date_format.parse(release_date);

            SimpleDateFormat new_date_format = new SimpleDateFormat("EEEE, MMM dd, yyyy");
            String date_of_release = new_date_format.format(date);
            tvRelease.setText(date_of_release);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadImage(MovieItems items) {
        if (items.getImageFilm() != null) {
            String image = POSTER_URL + items.getImageFilm();
            Picasso.get().load(image).into(imageMovie);
        }
        if (items.getImageFilm() != null) {
            String backdrop = BACKDROP_URL + items.getBackdropFilm();
            Picasso.get().load(backdrop).into(backdropMovie);
        }
    }
    private void loadDatabase() {
        helper = new FilmHelper(this);
        helper.open();

        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI + "/" + film.getId())
                , null
                , null
                , null
                , null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) isFavorite = true;
            cursor.close();
        }
        favoriteSet();
    }

}
