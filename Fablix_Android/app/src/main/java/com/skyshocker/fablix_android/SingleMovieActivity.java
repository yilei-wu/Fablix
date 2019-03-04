package com.skyshocker.fablix_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        title = findViewById(R.id.Title);
        year = findViewById(R.id.Year);
        director = findViewById(R.id.Director);
        stars = findViewById(R.id.Stars);
        genres = findViewById(R.id.Genres);

        back = findViewById(R.id.bback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
