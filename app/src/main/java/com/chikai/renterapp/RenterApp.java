package com.chikai.renterapp;

import android.app.Application;

import com.chikai.renterapp.net.HttpRequestUtils;

/**
 * Created by Administrator on 2017/7/25.
 */

public class RenterApp extends Application {
    public static RenterApp getInstance() {
        return instance;
    }

    private static RenterApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
}
