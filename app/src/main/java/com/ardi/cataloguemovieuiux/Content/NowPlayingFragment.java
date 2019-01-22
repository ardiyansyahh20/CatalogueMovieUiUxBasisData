package com.ardi.cataloguemovieuiux.Content;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ardi.cataloguemovieuiux.BuildConfig;
import com.ardi.cataloguemovieuiux.R;
import com.ardi.cataloguemovieuiux.Adapter.ContentAdapter;
import com.ardi.cataloguemovieuiux.entity.MovieItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment {

    private RecyclerView rvCategory;
    private RecyclerView.Adapter adapter;
    private View view;
    private ArrayList<MovieItems> movieLists;

    private static final String API_URL = BuildConfig.MOVIE_URL + "/now_playing?api_key=" + BuildConfig.MOVIE_API_KEY + "&language=en-US";

    public NowPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        rvCategory =  view.findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieLists = new ArrayList<>();
        loadUrlData();
        return view;
    }

    private void loadUrlData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        MovieItems movies = new MovieItems();

                        JSONObject data = array.getJSONObject(i);
                        movies.setId(data.getInt("id"));
                        movies.setTitleFilm(data.getString("title"));
                        movies.setOverviewFilm(data.getString("overview"));
                        movies.setRilisFilm(data.getString("release_date"));
                        movies.setImageFilm(data.getString("poster_path"));
                        movies.setVoteFilm(data.getString("vote_count"));
                        movies.setRatingFilm(data.getString("vote_average"));
                        movies.setBackdropFilm(data.getString("backdrop_path"));
                        movieLists.add(movies);
                    }

                    adapter = new ContentAdapter(movieLists, getActivity());
                    rvCategory.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                loadUrlData();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
