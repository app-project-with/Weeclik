package com.grace.weeclik.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * com.grace.weeclik.adapter
 * Created by grace on 01/07/2018.
 */
public class GalleryImagesAdapter extends BaseAdapter {

    private Context context;
    //List<String> listDataHeader; // header titles
    //HashMap<String, List<String>> listDataChild;
    String objectID;
    ArrayList<String> imageUrl = null;

    public GalleryImagesAdapter(Context context) {
        this.context = context;
        Intent intent = ((Activity) context).getIntent();
        objectID = intent.getExtras().getString("objectID");
        try {
            this.imageUrl = prepareData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getCount() {
        return imageUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Intent intent = ((Activity) context).getIntent();
        //objectID = intent.getExtras().getString("objectID");
        Log.e("TAG: ", "momomomo---> " + objectID);

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        /*for(int i=0; i<mesPhotos.size();i++){
            Glide.with(context).load(mesPhotos.get(i)).into(imageView);
        }*/
        Glide.with(context).load(imageUrl.get(position)).into(imageView);
        //imageView.setImageResource(thumbIds[position]);
        return imageView;
    }

    private ArrayList<String> prepareData() throws ParseException {
        final ArrayList<String> photo = new ArrayList<String>();
        ParseQuery<ParseObject> queryCommercePhoto = ParseQuery.getQuery("Commerce_Photos");
        queryCommercePhoto.whereContains("commerce",objectID);
        List<ParseObject> resultsphoto = null;
        try {
            resultsphoto = queryCommercePhoto.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("TAG: ", "taillleee---> " + resultsphoto.size());

        for(int i=0;i<resultsphoto.size();i++) {
            if(resultsphoto.get(i).getParseFile("photo") != null){
                photo.add(resultsphoto.get(i).getParseFile("photo").getUrl());
            }
        }
        return photo;
    }

}
