package com.ickkey.dztenant;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

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
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        aCache=ACache.get(getInstance());
        CrashHandler.getInstance().init(getApplicationContext());
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
    public LoginResponse getUserInfo(){
        if(userInfo==null){
            userInfo= (LoginResponse) aCache.getAsObject(ConstantValues.CACHE_KEY_TOKEN_USER_INFO);
        }
        return userInfo;

    }
    public void saveUserInfo (LoginResponse userInfo){
        this.userInfo=userInfo;
        aCache.put(ConstantValues.CACHE_KEY_TOKEN_USER_INFO,userInfo);


    }
    public void setPwd(String pwd){
        sharedPreferences.edit().putString("pwd",pwd).commit();
    }
    public String getPwd(){
       return sharedPreferences.getString("pwd",null);
    }
    public void logOut(){
        userInfo=null;
        aCache.clear();
        sharedPreferences.edit().clear().commit();


    }
}
