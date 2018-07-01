package com.grace.weeclik;

import android.app.Application;

import com.parse.Parse;

/**
 * com.grace.weeclik
 * Created by grace on 06/05/2018.
 */
public class ServerModel extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
    }
}
