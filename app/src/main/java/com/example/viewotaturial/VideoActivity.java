package com.example.viewotaturial;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = "VideoActivity";
    private RequestQueue mQuere;
    private String url = "https://anime2001.com/episode/koisuru-asteroid-%d8%a7%d9%84%d8%ad%d9%84%d9%82%d8%a9-3-2/";
    private VideoView video;
    private MediaController ctlr;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        video = findViewById(R.id.videoView);
        video.setVisibility(View.INVISIBLE);
        ctlr = new MediaController(this);

        ctlr.setVisibility(View.INVISIBLE);
        progressBar= findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);






        mQuere = MySingleton.getInstance(this).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressBar.setVisibility(View.INVISIBLE);
                    video.setVisibility(View.VISIBLE);
                    ctlr.setVisibility(View.VISIBLE);

                    Document document = Jsoup.parse(response);
                    Elements links = document.select("div.episode-videoplay");
                    for (Element link : links) {
                        String videourl = link.select("div.jw-media jw-reset")
                        .attr("src");



                        Uri uri = Uri.parse(videourl);

                        video.setVideoURI(uri);
                        ctlr.setAnchorView(video);
                        video.setMediaController(ctlr);
                        video.requestFocus();
                        video.start();
                         Log.e("items","title: " + videourl);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error in connection");
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
