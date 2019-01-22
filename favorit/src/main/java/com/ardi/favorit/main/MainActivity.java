package com.ardi.favorit.main;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ardi.favorit.R;
import com.ardi.favorit.adapter.FavoritFilmAdapter;
import com.ardi.favorit.database.DbContract;

import static com.ardi.favorit.database.DbContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FavoritFilmAdapter favoritFilmAdapter;
    ListView lvFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Favorite");
        lvFavorite = findViewById(R.id.lv_fav);
        favoritFilmAdapter = new FavoritFilmAdapter(this, null, true);
        lvFavorite.setAdapter(favoritFilmAdapter);
        lvFavorite.setOnItemClickListener(this);
        new loadMovieAsync().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new loadMovieAsync().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Cursor cursor = (Cursor) favoritFilmAdapter.getItem(position);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.favColumns._ID));
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.setData(Uri.parse(CONTENT_URI + "/" + id));
        startActivity(intent);
    }

    private class loadMovieAsync extends AsyncTask<Void, Void, Cursor>{

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            favoritFilmAdapter.swapCursor(cursor);
        }
    }
}
