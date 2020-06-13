package com.xyz.druber.flight.application;

import android.app.Application;
import android.content.Context;

import com.secneo.sdk.Helper;


/**
 * Created by SANDEEP
 */

public class BaseApplication extends Application {

    private AirCraftApplication airCraftApplication;
    private DJIBaseApplication djiBaseApplication;

    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        Helper.install(BaseApplication.this);
        if (airCraftApplication == null) {
            airCraftApplication = new AirCraftApplication();
            djiBaseApplication=new DJIBaseApplication();
            airCraftApplication.setContext(this);
            djiBaseApplication.setContext(this);

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        airCraftApplication.onCreate();
        djiBaseApplication.onCreate();
    }

}
