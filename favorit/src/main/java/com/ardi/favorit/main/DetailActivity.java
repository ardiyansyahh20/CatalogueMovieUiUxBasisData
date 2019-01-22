package com.ardi.favorit.main;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardi.favorit.BuildConfig;
import com.ardi.favorit.R;
import com.ardi.favorit.entity.MovieItems;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
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
    //    @BindView(R.id.fab)
//    FloatingActionButton fabFav;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collaps;


    private MovieItems film = null;
    Uri uri;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        film = getIntent().getParcelableExtra("film");
        uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) film = new MovieItems(cursor);
                cursor.close();
            }
        }

        if (film != null) {
            String poster = BuildConfig.POSTER_URL + film.getImageFilm();
            Picasso.get().load(poster)
                    .into(imageMovie);
            String backdrop = BuildConfig.BACKDROP_URL + film.getBackdropFilm();
            Picasso.get().load(backdrop)
                    .into(backdropMovie);
            collaps.setTitle(film.getTitleFilm());
            tvJudul.setText(film.getTitleFilm());
            tvOverview.setText(film.getOverviewFilm());
            tvRating.setText(getString(R.string.rating, film.getRatingFilm()));
            tvVote.setText(getString(R.string.vote, film.getVoteFilm()));
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            delete();
        }
        return super.onOptionsItemSelected(item);
    }

    public void delete() {
        String message = getResources().getString(R.string.dialog_delete);
        String judulDIalog = getResources().getString(R.string.judul_dialog);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(judulDIalog);
        alert.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContentResolver().delete(uri, null, null);
                        finish();
                    }

                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

}
