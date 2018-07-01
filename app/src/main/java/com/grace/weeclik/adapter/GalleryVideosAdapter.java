package com.grace.weeclik.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.grace.weeclik.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * com.grace.weeclik.adapter
 * Created by grace on 01/07/2018.
 */
public class GalleryVideosAdapter extends BaseAdapter {

    private Context context;
    // private MediaController mediaControls;
    String objectID;
    ArrayList<String> videoUrl = null;
    ArrayList<String> Namevideo = new ArrayList<>();

    public GalleryVideosAdapter(Context context) {
        this.context = context;
        Intent intent = ((Activity) context).getIntent();
        objectID = intent.getExtras().getString("objectID");
        this.videoUrl = prepareData();
    }

    @Override
    public int getCount() {
        return videoUrl.size();

    }

    @Override
    public Object getItem(int position) {
        return videoUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setLayoutParams(new GridView.LayoutParams(500, 500));
        } else {
            textView = (TextView) convertView;
        }







        textView.setText(Namevideo.get(position));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.parseColor("#1E88E5"));
        textView.setTextSize(20);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setBackgroundResource(R.drawable.ic_on_demand_video);
        return textView;
    }


    private ArrayList<String> prepareData(){







        ArrayList<String>  videos = new ArrayList<>();
        ParseQuery<ParseObject> queryCommercePhoto = ParseQuery.getQuery("Commerce_Videos");
        queryCommercePhoto.whereContains("leCommerce",objectID);
        List<ParseObject> resultsvideo = null;
        try {
            resultsvideo = queryCommercePhoto.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        for(int i=0;i<resultsvideo.size();i++) {
            if(resultsvideo.get(i).getParseFile("video") != null){
                String nameVideo = "";
                if(resultsvideo.get(i).getString("nameVideo")   != null ){
                    //Log.e("TAG: ", "oKKKKVideo---> " + resultsvideo.get(i).getString("nameVideo"));
                    nameVideo = resultsvideo.get(i).getString("nameVideo");
                    //Namevideo.add(resultsvideo.get(i).getString("nameVideo"));
                }else{
                    nameVideo = "video"+ (int)(Math.random() * 1000);
                    //Namevideo.add("video"+ (int)(Math.random() * 1000) );
                }
                Namevideo.add(nameVideo);
                videos.add(resultsvideo.get(i).getParseFile("video").getUrl());
                Log.e("TAG: ", "Video---> " + videos.size());


            }
        }
        return videos;
    }
}
