package com.ardi.cataloguemovieuiux.Adapter;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardi.cataloguemovieuiux.BuildConfig;
import com.ardi.cataloguemovieuiux.R;
import com.ardi.cataloguemovieuiux.entity.MovieItems;
import com.ardi.cataloguemovieuiux.listener.OnItemClickListener;
import com.ardi.cataloguemovieuiux.search.DetailFilmActivity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FavoritFilmAdapter extends RecyclerView.Adapter<FavoritFilmAdapter.ViewHolder> {
    private Context context;
    private Cursor lisFav;

    public FavoritFilmAdapter(Context context) {
        this.context = context;
    }

    public void replaceAll(Cursor items) {
        this.lisFav = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film_playing_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MovieItems movieItems = getItem(position);

        holder.judul.setText(movieItems.getTitleFilm());
        holder.overview.setText(movieItems.getOverviewFilm());
        holder.rilis.setText(movieItems.getRilisFilm());

        String release_date = movieItems.getRilisFilm();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = date_format.parse(release_date);

            SimpleDateFormat new_date_format = new SimpleDateFormat("E, MMM dd, yyyy");
            String date_of_release = new_date_format.format(date);
            holder.rilis.setText(date_of_release);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get()
                .load(BuildConfig.POSTER_URL + movieItems.getImageFilm())
                .into(holder.posterFilm);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieItems items = getItem(position);
                Intent intent = new Intent(context, DetailFilmActivity.class);
                intent.putExtra("film", items);
                context.startActivity(intent);
            }
        });

        holder.btnShare.setOnClickListener(new OnItemClickListener(position, new OnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, movieItems.getTitleFilm() + " \n" + movieItems.getOverviewFilm());
                intent.setType("text/plain");
                context.startActivity(intent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        if (lisFav == null) {
            return 0;
        }
        return lisFav.getCount();
    }

    private MovieItems getItem(int position) {
        if (!lisFav.moveToPosition(position)) {
            throw new IllegalStateException("position invalid");
        }
        return new MovieItems(lisFav);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView judul, overview, rilis;
        private ImageView posterFilm;
        private Button btnDetail, btnShare;

        private ViewHolder(View view) {
            super(view);

            judul = view.findViewById(R.id.tv_item_title);
            overview = view.findViewById(R.id.tv_item_overview);
            rilis = view.findViewById(R.id.tv_item_date);
            posterFilm = view.findViewById(R.id.img_item_poster);
            btnDetail = view.findViewById(R.id.btn_set_detail);
            btnShare = view.findViewById(R.id.btn_set_share);
        }
    }
}