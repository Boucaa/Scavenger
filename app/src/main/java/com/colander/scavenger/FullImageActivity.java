package com.colander.scavenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class FullImageActivity extends AppCompatActivity {

    public static String IMAGE_URL = "IMAGE_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.full_image);
        imageView.setImageUrl(getIntent().getStringExtra(IMAGE_URL), new ImageLoader(Volley.newRequestQueue(this), new BitmapLruCache()));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
