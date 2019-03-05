package com.skyshocker.fablix_android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieListActivity extends AppCompatActivity {
    public static final String MOVIE_ID = "SKYSHOCKER.MOVIE_ID";
    public static final int ITEM_PER_PAGE = 20;

    private EditText mSearchText;
    private ListView mMovieList;

    private String movieId = "";

    private int mCurrentPage = 1;
    private int mTotalPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mSearchText = findViewById(R.id.search_text);
        mMovieList = findViewById(R.id.movie_list);

        ((Button) findViewById(R.id.search_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        ((Button) findViewById(R.id.prev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPage();
            }
        });

        ((Button) findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();
            }
        });
    }

    private void updateMovieList(JSONObject response){
        ArrayList<String> serializedMovies = new ArrayList<>();

        try{
            mTotalPage = response.getInt("total_number") / ITEM_PER_PAGE;
            if(response.getInt("total_number") % ITEM_PER_PAGE != 0){
                mTotalPage ++;
            }

            JSONArray movies = response.getJSONArray("movielist");

            for (int i = 0; i < movies.length(); i++){
                String serializedMovie = "";
                JSONObject movie = movies.getJSONObject(i);

                movieId = movie.getString("movie_id");

//                title;year;director;genres;stars
//                serializedMovie += movie.getString("movie_id") + ";";
                serializedMovie += movie.getString("movie_title") + ";";
                serializedMovie += movie.getString("year") + ";";
                serializedMovie += movie.getString("director") + ";";
                serializedMovie += movie.getString("genre_list") + ";";

                JSONArray stars = movie.getJSONArray("star_list");
                String serializedStar = "";
                for (int j = 0; j < stars.length(); j++){
                    JSONObject starObj = stars.getJSONObject(j);
                    serializedStar += starObj.getString("star_name") + ", ";
                }
                serializedStar = serializedStar.substring(0, serializedStar.length() - 2);

                serializedMovie += serializedStar;
                serializedMovies.add(serializedMovie);
            }

        } catch (JSONException e) {
            System.out.println(e);
            System.out.println(response);
        }

        ArrayAdapter adapter = new MovieListAdapter(this, R.layout.movie_list_item, serializedMovies);
        mMovieList.setAdapter(adapter);

        mMovieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Intent intent = new Intent(MovieListActivity.this, SingleMovieActivity.class);
                intent.putExtra(MOVIE_ID, movieId);
                startActivity(intent);
            }
        });
    }

    private void nextPage(){
        if(mCurrentPage < mTotalPage){
            mCurrentPage +=1;
            search();
        }
    }

    private void lastPage(){
        if(mCurrentPage > 1){
            mCurrentPage -=1;
            search();
        }
    }

    private void search(){
        String keyword = mSearchText.getText().toString();

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        JsonObjectRequest searchRequest = new JsonObjectRequest(Request.Method.GET,
                "https://3.17.195.187:8443/fablix/api/keyword_search?sort=ta&keyword=" + keyword + "&page=" + mCurrentPage +
                "&records=" + ITEM_PER_PAGE,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("search_movie.response", response.toString());

                        updateMovieList(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("search_movie.error", error.toString());
                        error.printStackTrace();
                    }
                });

        queue.add(searchRequest);
    }

    private class MovieListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values;

        public MovieListAdapter(Context context, int textViewResourceId,
                                ArrayList<String> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View incidentView = inflater.inflate(R.layout.movie_list_item, parent, false);

            TextView titleText = incidentView.findViewById(R.id.title);
            TextView yearText = incidentView.findViewById(R.id.year);
            TextView directorText = incidentView.findViewById(R.id.director);
            TextView genresText = incidentView.findViewById(R.id.genres);
            TextView starsText = incidentView.findViewById(R.id.stars);

            titleText.setText(values.get(position).split(";")[0]);
            yearText.setText(values.get(position).split(";")[1]);
            directorText.setText(values.get(position).split(";")[2]);
            genresText.setText(values.get(position).split(";")[3]);
            starsText.setText(values.get(position).split(";")[4]);

            return incidentView;
        }
    }
}
