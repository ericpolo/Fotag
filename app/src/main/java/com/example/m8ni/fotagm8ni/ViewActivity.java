package com.example.m8ni.fotagm8ni;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.io.InputStream;

public class ViewActivity extends AppCompatActivity {
    ImageView im = null;
    RatingBar rb = null;
    Button reset = null;
    int rate = 0;
    String url = null;
    String name = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        im = findViewById(R.id.picture);
        rb = findViewById(R.id.rating);
        reset = findViewById(R.id.reset);
        reset.setVisibility(View.INVISIBLE);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = (int) rating;
                if(rating!=0){
                    reset.setVisibility(View.VISIBLE);
                } else {
                    reset.setVisibility(View.INVISIBLE);
                }
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("rating", rate);
                setResult(RESULT_OK, intent);
            }
        });
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        rate = intent.getIntExtra("rate", -1);
        name = intent.getStringExtra("name");
        rb.setRating(rate);
        new loader(im).execute(url);
    }
    public void onViewClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("rating", rate);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void onResetClick(View view){
        rate = 0;
        rb.setRating(0);
        reset.setVisibility(View.INVISIBLE);
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("rating", rate);
        setResult(RESULT_OK, intent);
    }
}

class loader extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public loader(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}