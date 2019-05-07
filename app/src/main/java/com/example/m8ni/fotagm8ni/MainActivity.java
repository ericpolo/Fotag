package com.example.m8ni.fotagm8ni;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import java.io.InputStream;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    final String [] images={"bunny.jpg","chinchilla.jpg","doggo.jpg","hamster.jpg","husky.jpg",
                        "kitten.png","loris.jpg","puppy.jpg","redpanda.jpg","sleepy.png"};
    final int[] viewIDs = {R.id.bunny_jpg,R.id.chinchilla_jpg,R.id.doggo_jpg,R.id.hamster_jpg,
            R.id.husky_jpg, R.id.kitten_png,R.id.loris_jpg,R.id.puppy_jpg,
            R.id.redpanda_jpg,R.id.sleepy_png};
    final int[] ratingIDs = {R.id.ratingbunny_jpg,R.id.ratingchinchilla_jpg,R.id.ratingdoggo_jpg,
            R.id.ratinghamster_jpg, R.id.ratinghusky_jpg, R.id.ratingkitten_png,
            R.id.ratingloris_jpg,R.id.ratingpuppy_jpg,R.id.ratingredpanda_jpg,R.id.ratingsleepy_png};
    final int[] layoutIDs = {R.id.llbunny_jpg,R.id.llchinchilla_jpg,R.id.lldoggo_jpg,R.id.llhamster_jpg,
            R.id.llhusky_jpg,R.id.llkitten_png,R.id.llloris_jpg,R.id.llpuppy_jpg,R.id.llredpanda_jpg,
            R.id.llsleepy_png};
    final int[] rIDs = {R.id.Rbunny_jpg,R.id.Rchinchilla_jpg,R.id.Rdogoo_jpg,R.id.Rhamster_jpg,
            R.id.Rhusky_jpg,R.id.Rkitten_png,R.id.Rloris_jpg,R.id.Rpuppy_jpg,R.id.Rredpanda_jpg,
            R.id.Rsleepy_png};
    String url = "https://www.student.cs.uwaterloo.ca/~cs349/f18/assignments/images/";
    Vector<ImageView> imView = new Vector<>();
    Vector<RatingBar> imRating = new Vector<>();
    Vector<LinearLayout> ll = new Vector<>();
    Vector<Button> rButtons = new Vector<>();
    RatingBar globalRb;
    Button clearFilter;
    int[] rate = {0,0,0,0,0,0,0,0,0,0};
    int globalRate = 0;
    boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }
    public void initialize(){
        for(int i = 0; i< viewIDs.length; i++){
            ImageView iv = findViewById(viewIDs[i]);
            iv.setVisibility(View.GONE);
            imView.add(iv);
        }
        for(int i = 0;i<ratingIDs.length;i++){
            RatingBar rb = findViewById(ratingIDs[i]);
            final int index = i;
            rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    for(int i = 0;i<ratingIDs.length;i++){
                        if(ratingBar.getId()==ratingIDs[i]) {
                            rate[i] = (int)rating;
                            if(rating!=0){
                                rButtons.get(i).setVisibility(View.VISIBLE);
                            } else {
                                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    rButtons.get(index).setVisibility(View.GONE);
                                } else {
                                    rButtons.get(index).setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                        if(globalRate!=0){
                            if(rate[i]<globalRate) {
                                imView.get(index).setVisibility(View.GONE);
                                imRating.get(index).setVisibility(View.GONE);
                                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    rButtons.get(index).setVisibility(View.GONE);
                                } else {
                                    rButtons.get(index).setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                }
            });
            rb.setVisibility(View.GONE);
            imRating.add(rb);
        }
        RatingBar rb = findViewById(R.id.ratingBar);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(!loaded) {
                    ratingBar.setRating(globalRate);
                    return;
                }
                globalRate = (int)rating;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if(rating!=0){
                        findViewById(R.id.clearFilter).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.clearFilter).setVisibility(View.GONE);
                    }
                    for (int i = 0; i < rate.length; i++) {
                        if (globalRate <= rate[i]) {
                            imView.get(i).setVisibility(View.VISIBLE);
                            imRating.get(i).setVisibility(View.VISIBLE);
                            if(rate[i]!=0){
                                rButtons.get(i).setVisibility(View.VISIBLE);
                            } else {
                                rButtons.get(i).setVisibility(View.GONE);
                            }
                        } else {
                            imView.get(i).setVisibility(View.GONE);
                            imRating.get(i).setVisibility(View.GONE);
                            rButtons.get(i).setVisibility(View.GONE);
                        }
                    }
                } else {
                    if(rating!=0){
                        findViewById(R.id.clearFilter).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.clearFilter).setVisibility(View.GONE);
                    }
                    GridLayout grid = findViewById(R.id.grid);
                    grid.removeAllViews();
                    for (int i = 0; i < ll.size(); i++) {
                        if (rate[i] >= rating) {
                            grid.addView(ll.get(i));
                        }
                    }
                }
            }
        });
        globalRb=rb;
        clearFilter = findViewById(R.id.clearFilter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            for(int i = 0;i<layoutIDs.length;i++){
                ll.add((LinearLayout) findViewById(layoutIDs[i]));
            }
        }
        for(int i= 0; i < rIDs.length;i++){
            rButtons.add((Button)findViewById(rIDs[i]));
            findViewById(rIDs[i]).setVisibility(View.INVISIBLE);
        }

    }
    public void onLoadButtonClick(View view){
        //System.out.println("Loading");
        for(int i = 0;i<imView.size();i++){
            imView.get(i).setVisibility(View.VISIBLE);
            imRating.get(i).setVisibility(View.VISIBLE);
            imRating.get(i).setRating(0);
            new loader(imView.get(i)).execute(url+images[i]);
        }
        globalRb.setRating(0);
        globalRate=0;
        clearFilter.setVisibility(View.GONE);
        loaded = true;
    }
    public void onClearButtonClick(View view){
        for(int i = 0;i<imView.size();i++){
            imView.get(i).setVisibility(View.GONE);
            imRating.get(i).setVisibility(View.GONE);
            imRating.get(i).setRating(0);
        }
        globalRb.setRating(0);
        globalRate=0;
        clearFilter.setVisibility(View.GONE);
        loaded = false;
    }

    public void onClearFilterClick(View view){
        globalRb.setRating(0);
        globalRate = 0;
        if(loaded) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                for (int i = 0; i < imView.size(); i++) {
                    imView.get(i).setVisibility(View.VISIBLE);
                    imRating.get(i).setVisibility(View.VISIBLE);
                }
            } else {
                GridLayout grid = findViewById(R.id.grid);
                grid.removeAllViews();
                for(int i = 0;i<ll.size();i++){
                    imView.get(i).setVisibility(View.VISIBLE);
                    imRating.get(i).setVisibility(View.VISIBLE);
                    grid.addView(ll.get(i));
                }
            }
        }
        clearFilter.setVisibility(View.GONE);
    }

    public void onImageViewClick(View view){
        String name = null;
        int rating = 0;
        for(int i = 0;i<viewIDs.length;i++){
            if(view.getId()==viewIDs[i]){
                name = images[i];
                rating = rate[i];
                break;
            }
        }
        Intent intent = new Intent(this,ViewActivity.class);
        intent.putExtra("url",url+name);
        intent.putExtra("name",name);
        intent.putExtra("rate",rating);
        startActivityForResult(intent,0);
    }

    public void onResetButtonClick(View view){
        int id = view.getId();
        for(int i = 0;i<rIDs.length;i++){
            if(rIDs[i]==id){
                imRating.get(i).setRating(0);
                rate[i] = 0;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    rButtons.get(i).setVisibility(View.GONE);
                } else {
                    rButtons.get(i).setVisibility(View.INVISIBLE);
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (globalRb.getRating() != 0) {
                        imView.get(i).setVisibility(View.GONE);
                        imRating.get(i).setVisibility(View.GONE);
                    }
                } else {
                    GridLayout grid = findViewById(R.id.grid);
                    grid.removeAllViews();
                    for (int j = 0; j < ll.size(); j++) {
                        if (rate[j] >= globalRate) {
                            grid.addView(ll.get(j));
                        }
                    }
                }
                break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name = data.getStringExtra("name");
        int rating = data.getIntExtra("rating", 0);
        for (int i = 0; i < images.length; i++) {
            if (images[i].equals(name)) {
                rate[i] = rating;
                imRating.get(i).setRating(rating);
                if(rate[i]==0) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        rButtons.get(i).setVisibility(View.GONE);
                    } else {
                        rButtons.get(i).setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayout grid = findViewById(R.id.grid);
            grid.removeAllViews();
            for (int j = 0; j < ll.size(); j++) {
                if (rate[j] >= globalRate) {
                    grid.addView(ll.get(j));
                }
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("rate", rate);
        outState.putBoolean("loaded", loaded);
        outState.putInt("globalrate",globalRate);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        loaded = savedInstanceState.getBoolean("loaded");
        rate = savedInstanceState.getIntArray("rate");
        globalRate = savedInstanceState.getInt("globalrate");
        globalRb.setRating(globalRate);
        if (loaded) {
            for (int i = 0; i < 10; i++) {
                imRating.elementAt(i).setRating(rate[i]);
            }
        }
        if (loaded) {
            for(int i = 0;i<imView.size();i++){
                imView.get(i).setVisibility(View.VISIBLE);
                imRating.get(i).setVisibility(View.VISIBLE);
                if(rate[i]!=0) rButtons.get(i).setVisibility(View.VISIBLE);
                if(globalRate!=0) {
                    if(rate[i]<globalRate){
                        imView.get(i).setVisibility(View.GONE);
                        imRating.get(i).setVisibility(View.GONE);
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            rButtons.get(i).setVisibility(View.GONE);
                        } else {
                            rButtons.get(i).setVisibility(View.INVISIBLE);
                        }
                    }
                }
                new loader(imView.get(i)).execute(url+images[i]);
            }
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
}
