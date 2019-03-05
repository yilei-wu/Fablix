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
    public static final int ITEM_PER_PAGE = 20;

    private EditText mSearchText;
    private ListView mMovieList;

    private int mCurrentPage = 1;

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

    private void updateMovieList(JSONArray movies){

    }

    private void nextPage(){

    }

    private void lastPage(){

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
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View incidentView = inflater.inflate(R.layout.movie_list_item, parent, false);
            TextView dateText = incidentView.findViewById(R.id.date);
            TextView titleText = incidentView.findViewById(R.id.title);

            dateText.setText(values.get(position).split(";")[1]);
            titleText.setText(values.get(position).split(";")[0]);

            return incidentView;
        }
    }
}
