package com.ardi.cataloguemovieuiux.main;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ardi.cataloguemovieuiux.Adapter.FavoritFilmAdapter;
import com.ardi.cataloguemovieuiux.R;

import static com.ardi.cataloguemovieuiux.Database.DbContract.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritFragment extends Fragment {
    private Context context;
    private RecyclerView rvFav;
    private FavoritFilmAdapter adapter;

    public FavoritFragment(){
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit, container, false);

        context = view.getContext();
        rvFav = view.findViewById(R.id.rv_favorit);

        adapter = new FavoritFilmAdapter(context);
        rvFav.setLayoutManager(new LinearLayoutManager(context));
        rvFav.setAdapter(adapter);

        new loadMovieAsync().execute();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadMovieAsync().execute();
    }

    private class loadMovieAsync extends AsyncTask<Void, Void, Cursor>{

        @Override
        protected Cursor doInBackground(Void... voids) {
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor fav) {
            super.onPostExecute(fav);
            adapter.replaceAll(fav);

            if (fav.getCount() == 0){
                showSnackbarMessage(getString(R.string.datakosong));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(rvFav, message, Snackbar.LENGTH_SHORT).show();
    }
}
