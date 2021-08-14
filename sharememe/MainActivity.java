package com.example.android.sharememe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ImageView memeImage;
    private ProgressBar progressBar;
    private String currentImgUrl;
    private String rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memeImage=findViewById(R.id.memeImage);
        progressBar=findViewById(R.id.progressBar);
        loadmeme();
    }
    private void loadmeme(){
        progressBar.setVisibility(View.VISIBLE);

        rv="meme"; //test
        String url ="https://"+rv+"-api.herokuapp.com/gimme";

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentImgUrl=response.getString("url");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //progressBar.setVisibility(findViewById(R.id.progressBar).GONE);

                        Glide.with(MainActivity.this).load(currentImgUrl).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                               progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(memeImage);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        progressBar.setVisibility(View.GONE);
                        memeImage.setImageResource(R.drawable.ic_baseline_signal_cellular_connected_no_internet_4_bar_24);
                            
//                        Intent i=new Intent(MainActivity.this,networkError.class);
//                        startActivity(i);
                    }
                });

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    public void nextButton(View view) {

        loadmeme();
    }

    public void shareButton(View view) {
        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"hey! check out this funny meme..." + currentImgUrl);
        Intent.createChooser(intent,"share fun using...");
        startActivity(intent);
    }
}
