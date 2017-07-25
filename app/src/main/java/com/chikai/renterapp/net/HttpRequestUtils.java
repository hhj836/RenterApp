package com.chikai.renterapp.net;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.chikai.renterapp.RenterApp;


/**
 * Created by Administrator on 2017/7/25.
 */

public class HttpRequestUtils {
    private static HttpRequestUtils instance;
    private static RequestQueue mRequestQueue;
    public HttpRequestUtils(){
        mRequestQueue = Volley.newRequestQueue(RenterApp.getInstance());
    }
    public static HttpRequestUtils getInstance() {
        if (instance == null) {
            synchronized (HttpRequestUtils.class) {
                if (instance == null) {
                    instance = new HttpRequestUtils();

                }
            }

        }
        return instance;

    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }
    public static void addRequest(GsonRequest request, String tag) {
        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(20*1000, 0,1f));
        getRequestQueue().add(request);
    }
}