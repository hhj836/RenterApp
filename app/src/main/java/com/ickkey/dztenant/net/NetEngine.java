package com.ickkey.dztenant.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.ickkey.dztenant.net.response.BaseResponse;
import com.ickkey.dztenant.net.response.LoginResponse;
import com.ickkey.dztenant.utils.Json2ObjHelper;
import com.ickkey.dztenant.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/25.
 */

public class NetEngine {
    /**
     * 登录
     */
    public static  void sendLoginRequest(Context context, final OnResponseListener<LoginResponse> onResponseListener, String tag, Map<String,Object>...params){
        sendPostRequest(Urls.LOGIN,context,LoginResponse.class,onResponseListener,tag,params);
    }

    public static  <T extends BaseResponse> void sendPostRequest(String url,Context context, Class<T> parseClass, final OnResponseListener<T> onResponseListener, String tag, Map<String,Object>...params){
        String requestUrl=url;
        if(params.length>0){
            requestUrl+="?";
            for (Map.Entry<String, Object> entry : params[0].entrySet()) {
                requestUrl+=entry.getKey()+"="+entry.getValue()+"&";

            }
            requestUrl=requestUrl.substring(0,requestUrl.length()-1);
            LogUtil.info(NetEngine.class,"requestUrl=post="+requestUrl);

        }
        GsonRequest request=new GsonRequest(Request.Method.POST,requestUrl,parseClass,new Response.Listener<T>(){

            @Override
            public void onResponse(T response) {
                parseResponse(onResponseListener,response);
            }
        },new CommonVolleyErrorListener(context));
        HttpRequestUtils.getInstance().addRequest(request,tag);

    }
    public static <T extends BaseResponse> void sendGetRequest(String url,Context context,Class<T> parseClass, final OnResponseListener<T> onResponseListener, String tag, Map<String,Object>...params){
        String requestUrl=url;
        if(params.length>0){
            requestUrl+="?";
            for (Map.Entry<String, Object> entry : params[0].entrySet()) {
                requestUrl+=entry.getKey()+"="+entry.getValue()+"&";

            }
            requestUrl=requestUrl.substring(0,requestUrl.length()-1);
            LogUtil.info(NetEngine.class,"requestUrl=get="+requestUrl);

        }
        GsonRequest request=new GsonRequest(requestUrl, parseClass,new Response.Listener<T>(){

            @Override
            public void onResponse(T response) {
                parseResponse(onResponseListener,response);
            }
        },new CommonVolleyErrorListener(context));
        HttpRequestUtils.getInstance().addRequest(request,tag);

    }
    private static <T extends BaseResponse> void parseResponse(OnResponseListener onResponseListener, T response){
        if(response.code!=0){
            onResponseListener.onError(response.msg);

        }else {
            onResponseListener.onSucceed(response);
        }

    }

}
