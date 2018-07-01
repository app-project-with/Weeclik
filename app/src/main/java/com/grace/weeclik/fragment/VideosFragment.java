package com.grace.weeclik.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.grace.weeclik.R;
import com.grace.weeclik.VideoActivity;
import com.grace.weeclik.adapter.GalleryVideosAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VideosFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    GridView gridView;
    public String ObjectID= "";

    public VideosFragment() {
        // Required empty public constructor
    }

    public static VideosFragment newInstance(int sectionNumber) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        gridView = view.findViewById(R.id.grid_view_videos);
        gridView.setAdapter(new GalleryVideosAdapter(view.getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG: ", "---> " + i);
                Intent intent = ((Activity) Objects.requireNonNull(getContext())).getIntent();
                ObjectID = Objects.requireNonNull(intent.getExtras()).getString("objectID");
                Log.e("TAG: ", "---> " + ObjectID);
                Intent intent_image = new Intent(getContext(), VideoActivity.class);
                intent_image.putExtra("objectID",prepareData().get(i));
                startActivity(intent_image);
            }
        });

        return view;
    }

    private ArrayList<String> prepareData(){
        ArrayList<String>  videos = new ArrayList<>();
        ParseQuery<ParseObject> queryCommercePhoto = ParseQuery.getQuery("Commerce_Videos");
        queryCommercePhoto.whereContains("leCommerce",ObjectID);
        List<ParseObject> resultsvideo = null;
        try {
            resultsvideo = queryCommercePhoto.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert resultsvideo != null;
        Log.e("TAG: ", "taillleeeVideo---> " + resultsvideo.size());

        for(int i=0;i<resultsvideo.size();i++) {
            if(resultsvideo.get(i).getParseFile("video") != null){
                videos.add(resultsvideo.get(i).getParseFile("video").getUrl());
                Log.e("TAG: ", "Video---> " + videos.size());
            }
        }
        return videos;
    }

}
