package com.ardi.favorit.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardi.favorit.BuildConfig;
import com.ardi.favorit.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ardi.favorit.database.DbContract.favColumns.JUDUL;
import static com.ardi.favorit.database.DbContract.favColumns.OVERVIEW;
import static com.ardi.favorit.database.DbContract.favColumns.POSTER;
import static com.ardi.favorit.database.DbContract.favColumns.RILIS;
import static com.ardi.favorit.database.DbContract.getColumnString;

public class FavoritFilmAdapter extends CursorAdapter {

    @BindView(R.id.tv_item_title)
    TextView tvJudul;
    @BindView(R.id.tv_item_date)
    TextView tvRilis;
    @BindView(R.id.tv_item_overview)
    TextView tvOverview;
    @BindView(R.id.img_item_poster)
    ImageView imagePoster;



    public FavoritFilmAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_playing_list, parent, false);
        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null) {
            ButterKnife.bind(this,view);

            tvJudul.setText(getColumnString(cursor, JUDUL));
            tvRilis.setText(getColumnString(cursor, RILIS));
            tvOverview.setText(getColumnString(cursor, OVERVIEW));


            String release_date = getColumnString(cursor, RILIS);
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = date_format.parse(release_date);

                SimpleDateFormat new_date_format = new SimpleDateFormat("EEEE, MMM dd, yyyy");
                String date_of_release = new_date_format.format(date);
                tvRilis.setText(date_of_release);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Picasso.get().load(BuildConfig.POSTER_URL + getColumnString(cursor, POSTER)).into(imagePoster);

        }


    }
}