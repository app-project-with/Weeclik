package com.grace.weeclik;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.grace.weeclik.view.ZoomageView;

public class ImageZoomActivity extends AppCompatActivity {

    private ZoomageView demoView;
    private View optionsView;
    private AlertDialog optionsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String thumbnail = getIntent().getExtras().getString("thumbnail");
        setContentView(R.layout.activity_image_zoom);

        demoView = (ZoomageView)findViewById(R.id.demoView);
        Glide.with(this).load(thumbnail).into(demoView);
    }
}
