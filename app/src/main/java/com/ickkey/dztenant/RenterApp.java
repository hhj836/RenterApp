package com.ickkey.dztenant;

import android.app.Application;


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
