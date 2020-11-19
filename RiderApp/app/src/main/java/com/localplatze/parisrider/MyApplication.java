package com.localplatze.parisrider;

import android.app.Application;
import android.content.Context;

import com.mapbox.mapboxsdk.Mapbox;

public class MyApplication extends Application {
    private static Context context;
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        Mapbox.getInstance(this, getString(R.string.access_token));
    }
}