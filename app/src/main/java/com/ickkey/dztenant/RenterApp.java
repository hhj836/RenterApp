package com.ickkey.dztenant;

import android.support.multidex.MultiDexApplication;


/**
 * Created by Administrator on 2017/7/25.
 */

public class RenterApp extends MultiDexApplication {
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
