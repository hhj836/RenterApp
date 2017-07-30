package com.ickkey.dztenant.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.ickkey.dztenant.BuildConfig;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.net.request.BaseRequest;
import com.ickkey.dztenant.net.response.BaseResponse;
import com.ickkey.dztenant.utils.LogUtil;
import com.ickkey.dztenant.utils.NetUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hhj
 * @date 2017/7/31
 * @Description:
 */


public class BaseNetEngine {

    public   <T extends BaseResponse> void sendPostRequest(String url, Context context, Class<T> parseClass, final OnResponseListener<T> onResponseListener, String tag,  BaseRequest...req){
        if(!NetUtil.checkNetWork(context)){
            onResponseListener.onError(context.getString(R.string.no_net));
            return;
        }
        String requestUrl=url;
        Map<String,Object> paramsMap=new HashMap<>();
        if(req.length>0){
            if(BuildConfig.DEBUG){
                checkRespClass(req[0].getClass());
            }
            reflect(req[0].getClass(),paramsMap,req[0]);
        }
        if(paramsMap.size()>0){
            requestUrl+="?";
            for (Map.Entry<String, Object> entry :paramsMap.entrySet()) {
                try {
                    requestUrl+=entry.getKey()+"="+entry.getValue()instanceof String? URLEncoder.encode((String) entry.getValue(),"utf-8"):entry.getValue()+"&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

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
    public  <T extends BaseResponse> void sendGetRequest(String url,Context context,Class<T> parseClass, final OnResponseListener<T> onResponseListener, String tag,  BaseRequest...req){
        if(!NetUtil.checkNetWork(context)){
            onResponseListener.onError(context.getString(R.string.no_net));
            return;
        }
        String requestUrl=url;
        Map<String,Object> paramsMap=new HashMap<>();
        if(req.length>0){
            if(BuildConfig.DEBUG){
                checkRespClass(req[0].getClass());
            }
            reflect(req[0].getClass(),paramsMap,req[0]);
        }
        if(paramsMap.size()>0){
            requestUrl+="?";
            for (Map.Entry<String, Object> entry :paramsMap.entrySet()) {
                try {
                    requestUrl+=entry.getKey()+"="+entry.getValue()instanceof String? URLEncoder.encode((String) entry.getValue(),"utf-8"):entry.getValue()+"&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

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
    private  <T extends BaseResponse> void parseResponse(OnResponseListener onResponseListener, T response){
        if(response.code!=0){
            onResponseListener.onError(response.msg);

        }else {
            onResponseListener.onSucceed(response);
        }

    }
    private  void reflect(final Class clazz, final Map<String,Object> params,
                        final Object requestObj) {
        if (clazz == null || params == null) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value =field.get(requestObj);
                if (value != null) {
                    params.put(field.getName(),value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Class superclass = clazz.getSuperclass();
        if (superclass != null) {
            reflect(superclass, params, requestObj);
        }
    }
    private  void checkRespClass(Class clazz){
        if(!(clazz instanceof Serializable)){
            throw new RuntimeException("--同学--"+clazz.getSimpleName()+"--没实现Serializable");
        }


        Class[] classes=clazz.getDeclaredClasses();
        for(Class cls:classes){
            if(!(cls instanceof Serializable)){
                throw new RuntimeException("--同学--"+cls.getSimpleName()+"--没实现Serializable");
            }
        }



    }
}
