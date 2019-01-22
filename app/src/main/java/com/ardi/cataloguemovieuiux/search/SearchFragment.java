package com.ardi.cataloguemovieuiux.search;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.ardi.cataloguemovieuiux.R;
import com.ardi.cataloguemovieuiux.Adapter.MovieAdapter;
import com.ardi.cataloguemovieuiux.entity.MovieItems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {

    @BindView(R.id.lv_movie)
    ListView lvMovie;
    @BindView(R.id.edt_movie)
    EditText edtTitle;
    @Nullable
    @BindView(R.id.image_detail)
    ImageView imgMovie;
    @BindView(R.id.btn_find)
    FancyButton btnFindMovie;
    MovieAdapter adapter;
    private View view;
    private MovieItems film;
    private Context context;
    static final String EXTRAS_MOVIE = "extras_movie";

    public SearchFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        adapter = new MovieAdapter(getActivity());
        adapter.notifyDataSetChanged();

        lvMovie.setAdapter(adapter);
        lvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                MovieItems items = (MovieItems) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailFilmActivity.class);
                intent.putExtra("film", items);


                getActivity().startActivity(intent);
            }
        });

        btnFindMovie.setOnClickListener(movieListener);

        String judul_film = edtTitle.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_MOVIE, judul_film);
        LoaderManager.getInstance(this).initLoader(0, bundle, SearchFragment.this);

        return view;
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int i, Bundle bundle) {
        String movieJudul = "";
        if (bundle != null) {
            movieJudul = bundle.getString(EXTRAS_MOVIE);
        }
        return new MovieAsyncTaskLoader(getActivity(), movieJudul);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> movieItems) {
        adapter.setData(movieItems);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<MovieItems>> loader) {
        adapter.setData(null);
    }

    final View.OnClickListener movieListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String title_film = edtTitle.getText().toString();
            if (TextUtils.isEmpty(title_film)) {
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString(EXTRAS_MOVIE, title_film);
            getLoaderManager().restartLoader(0, bundle, SearchFragment.this);
        }
    };
}
