package com.chikai.renterapp.net;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.chikai.renterapp.net.response.BaseResponse;
import com.chikai.renterapp.net.response.TestResponseBean;
import com.chikai.renterapp.utils.Json2ObjHelper;
import com.chikai.renterapp.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/25.
 */

public class NetEngine {
    /**
     * 测试网络请求
     */
    public static  void sendTestRequest(Context context, final OnResponseListener<TestResponseBean> onResponseListener, String tag, Map<String,Object>...params){
        sendGetRequest(context,TestResponseBean.class,onResponseListener,tag,params);
    }

    public static  <T extends BaseResponse> void sendPostRequest(Context context,Class<T> parseClass, final OnResponseListener<T> onResponseListener, String tag, Map<String,Object>...params){
        String url="http://baidu.com";
        Map<String ,String> map=null;
        if(params.length>0){
            map=new HashMap<>();
            map.put("data", Json2ObjHelper.getGson().toJson(params[0]));
        }
        GsonRequest request=new GsonRequest(Request.Method.POST,url,parseClass,new Response.Listener<T>(){

            @Override
            public void onResponse(T response) {
                parseResponse(onResponseListener,response);
            }
        },new CommonVolleyErrorListener(context),map);
        HttpRequestUtils.getInstance().addRequest(request,tag);

    }
    public static <T extends BaseResponse> void sendGetRequest(Context context,Class<T> parseClass, final OnResponseListener<T> onResponseListener, String tag, Map<String,Object>...params){
        String url="http://baidu.com";
        if(params.length>0){
            url+="?";
            for (Map.Entry<String, Object> entry : params[0].entrySet()) {
                url+=entry.getKey()+"="+entry.getValue()+"&";

            }
            url=url.substring(0,url.length()-1);
            LogUtil.info(NetEngine.class,"url=="+url);

        }

        GsonRequest request=new GsonRequest(url, parseClass,new Response.Listener<T>(){

            @Override
            public void onResponse(T response) {
                parseResponse(onResponseListener,response);
            }
        },new CommonVolleyErrorListener(context));
        HttpRequestUtils.getInstance().addRequest(request,tag);

    }
    private static <T extends BaseResponse> void parseResponse(OnResponseListener onResponseListener, T response){
        if(response.code!=0){
            onResponseListener.onError(response.errorMsg);

        }else {
            onResponseListener.onSucceed(response);
        }

    }

}
