package com.skyshocker.fablix_android;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


public class SingleMovieActivity extends AppCompatActivity {

    //ui reference
    private EditText title;
    private EditText year;
    private EditText director;
    private EditText stars;
    private EditText genres;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        String movie_id = new String();//
        title = findViewById(R.id.Title);
        year = findViewById(R.id.Year);
        director = findViewById(R.id.Director);
        stars = findViewById(R.id.Stars);
        genres = findViewById(R.id.Genres);
        load_info(movie_id);

        back = findViewById(R.id.bback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void load_info(String movie_id)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://3.17.195.187:8443/fablix/api/single_movie" + "?id=" + movie_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("single_movie", response);
                try
                {
                    JSONObject result = new JSONObject(response);
                    String ryear = result.getString("year");
                    String rtitle = result.getString("title");
                    String rdirector = result.getString("director");
                    String genre_array = result.getString("genre_list");
                    JSONArray star_array = (JSONArray)result.get("star_list");

                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 0; i < star_array.length(); i++)
                    {
                        stringBuilder.append(star_array.getJSONObject(i).get("star_name") + " ");
                    }
                    title.setText(rtitle);
                    year.setText(ryear);
                    director.setText(rdirector);
                    stars.setText(stringBuilder);
                    genres.setText(genre_array);
                }
                catch (Exception e)
                {
                    System.out.println("error_message:" + e.getMessage());
                    System.out.println(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("single_movei.error", error.toString());
            }
        });
    }
}
