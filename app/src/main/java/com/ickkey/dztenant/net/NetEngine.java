package com.ickkey.dztenant.net;

import android.content.Context;

import com.ickkey.dztenant.net.request.BaseRequest;
import com.ickkey.dztenant.net.response.LoginResponse;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/25.
 */

public class NetEngine extends BaseNetEngine {
    private static NetEngine instance=new NetEngine();

    public static NetEngine getInstance() {
        if (instance == null) {
            instance = new NetEngine();
        }
        return instance;
    }
    /**
     * 登录
     */
    public   void sendLoginRequest(Context context, final OnResponseListener<LoginResponse> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.LOGIN,context,LoginResponse.class,onResponseListener,tag,req);
    }



}
