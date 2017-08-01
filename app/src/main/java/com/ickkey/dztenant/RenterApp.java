package com.ickkey.dztenant;

import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.ickkey.dztenant.net.request.LoginReq;
import com.ickkey.dztenant.net.response.LoginResponse;
import com.ickkey.dztenant.utils.cache.ACache;


/**
 * Created by Administrator on 2017/7/25.
 */

public class RenterApp extends MultiDexApplication {
    public Handler getMainThreadHandler() {
        return handler;
    }


    private   Handler handler=new Handler(Looper.getMainLooper());
    public static RenterApp getInstance() {
        return instance;
    }

    private static RenterApp instance;

    public ACache getCache() {
        return aCache;
    }

    private ACache aCache;
    private LoginResponse userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        aCache=ACache.get(getInstance());
        CrashHandler.getInstance().init(getApplicationContext());
    }
    public LoginResponse getUserInfo(){
        if(userInfo==null){
            userInfo= (LoginResponse) aCache.getAsObject(ConstantValue.CACHE_KEY_TOKEN_USER_INFO);
        }
        return userInfo;

    }
    public void saveUserInfo (LoginResponse userInfo){
        this.userInfo=userInfo;
        aCache.put(ConstantValue.CACHE_KEY_TOKEN_USER_INFO,userInfo);


    }
    public void logOut(){
        aCache.clear();

    }
}
