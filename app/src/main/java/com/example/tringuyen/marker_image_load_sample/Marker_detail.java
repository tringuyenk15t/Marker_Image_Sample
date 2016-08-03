package com.example.tringuyen.marker_image_load_sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tringuyen.marker_image_load_sample.models.DirectionResponse;
import com.example.tringuyen.marker_image_load_sample.network.Google_direction;
import com.example.tringuyen.marker_image_load_sample.network.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Marker_detail extends AppCompatActivity {

    private TextView txtID;
    private Google_direction google_direction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Map");
        setSupportActionBar(toolbar);

        txtID = (TextView) findViewById(R.id.location_id);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        txtID.setText(id);

        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: back to map and add direction
            }
        });

        google_direction = ServiceGenerator.createService(Google_direction.class);

        Call<DirectionResponse> call = google_direction.getDirections("10.7718488,106.6576495","10.775727,106.704895");
        call.enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                DirectionResponse result = response.body();
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}
