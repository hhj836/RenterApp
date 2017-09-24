package com.ickkey.dztenant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.ickkey.dztenant.activity.GestureLoginActivity;
import com.ickkey.dztenant.activity.MainActivity;
import com.ickkey.dztenant.base.BaseFragment;
import com.ickkey.dztenant.fragment.gesture.CreateGestureFragment;
import com.ickkey.dztenant.fragment.gesture.GestureLoginFragment;
import com.ickkey.dztenant.fragment.gesture.LoginPwdCheckFragment;
import com.ickkey.dztenant.fragment.login.LaunchFragment;
import com.ickkey.dztenant.fragment.login.LoginFragment;
import com.ickkey.dztenant.fragment.login.RegisterFragment;
import com.ickkey.dztenant.net.response.LoginResponse;
import com.ickkey.dztenant.utils.LogUtil;
import com.ickkey.dztenant.utils.cache.ACache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created by Administrator on 2017/7/25.
 */

public class RenterApp extends MultiDexApplication {
    public Handler getMainThreadHandler() {
        return handler;
    }

    public Map<Class,BaseFragment> fragmentMap=new HashMap<>();
    public Map<Class,SupportActivity> activityMap=new HashMap<>();
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
    private int mFinalCount;

    public void set_mActivity(SupportActivity _mActivity) {
        this._mActivity = _mActivity;
    }

    private SupportActivity _mActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            // 栈视图等功能，建议在Application里初始化
            Fragmentation.builder()
                    // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                    .stackViewMode(Fragmentation.BUBBLE)
                    .debug(BuildConfig.DEBUG).install();
        }
        instance=this;
        aCache=ACache.get(getInstance());
        CrashHandler.getInstance().init(getApplicationContext());
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mFinalCount++;
                //如果mFinalCount ==1，说明是从后台到前台

                if (mFinalCount == 1){
                    //说明从后台回到了前台
                    LogUtil.info(getClass(), mFinalCount +"说明从后台回到了前台");
                    LogUtil.info(getClass(), _mActivity +"=_mActivity"+"=isShowGesture="+isShowGesture()+"=" + "userInfo=="+userInfo);
                    if(_mActivity!=null&&isShowGesture()&&!TextUtils.isEmpty(getPwd())){
                                Intent intent=new Intent();
                                intent.setClass(_mActivity, GestureLoginActivity.class);
                                _mActivity.startActivity(intent);
                                LogUtil.info(getClass(), "打开----GestureLoginActivity");

                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                //如果mFinalCount ==0，说明是前台到后台

                if (mFinalCount == 0){
                    //说明从前台回到了后台
                    LogUtil.info(getClass(), mFinalCount +"说明从前台回到了后台");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
    public boolean isShowGesture(){
        return  _mActivity.findFragment(CreateGestureFragment.class)==null&&_mActivity.findFragment(GestureLoginFragment.class)==null
                &&_mActivity.findFragment(RegisterFragment.class)==null&&_mActivity.findFragment(LoginPwdCheckFragment.class)==null
                &&_mActivity.findFragment(LaunchFragment.class)==null
                &&!activityMap.containsKey(GestureLoginActivity.class);
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
    public void addFragment(BaseFragment fragment){
        fragmentMap.put(fragment.getClass(),fragment);
    }
    public void removeFragment(BaseFragment fragment){
        LogUtil.info(getClass(),"移除--"+fragment.getClass());
        fragmentMap.remove(fragment);
    }
    public void setPwd(String pwd){
        sharedPreferences.edit().putString("pwd",pwd).commit();
    }
    public String getPwd(){
       return sharedPreferences.getString("pwd",null);
    }
    public void logOut(){
        userInfo=null;
        sharedPreferences.edit().clear().commit();
      /*  if(fragmentActivity.length>0){
            for (Map.Entry<Class, BaseFragment> entry : fragmentMap.entrySet()) {
                BaseFragment fragment=entry.getValue();
                if(fragment.getClass()== LoginFragment.class){
                    continue;
                }
                fragmentActivity[0].getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            }
        }*/
        fragmentMap.clear();

    }
}
