package com.grace.weeclik.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.grace.weeclik.ImageZoomActivity;
import com.grace.weeclik.R;
import com.grace.weeclik.adapter.GalleryImagesAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ImagesFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    GridView gridView;
    public String ObjectID= "";

    public ImagesFragment() {
        // Required empty public constructor
    }

    public static ImagesFragment newInstance(int sectionNumber) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        gridView = view.findViewById(R.id.grid_view_images);
        gridView.setAdapter(new GalleryImagesAdapter(view.getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG: ", "---> " + i);
                Intent intent = ((Activity) Objects.requireNonNull(getContext())).getIntent();
                ObjectID = Objects.requireNonNull(intent.getExtras()).getString("objectID");
                try {
                    Intent intent_image = new Intent(getContext(), ImageZoomActivity.class);
                    intent_image.putExtra("thumbnail",prepareData().get(i));
                    startActivity(intent_image);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private ArrayList<String> prepareData() throws ParseException {
        final ArrayList<String> photo = new ArrayList<String>();
        ParseQuery<ParseObject> queryCommercePhoto = ParseQuery.getQuery("Commerce_Photos");
        queryCommercePhoto.whereContains("commerce",ObjectID);
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
