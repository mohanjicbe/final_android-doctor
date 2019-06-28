package com.orane.docassist;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.orane.docassist.Model.Model;

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //-----------------Init Flurry --------------------------
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogLevel(Log.INFO);
        FlurryAgent.setVersionName(Model.app_ver);
        FlurryAgent.init(this, Model.FLURRY_APIKEY);
        //-----------------Init Flurry --------------------------
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}