package com.chikai.renterapp.net;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2017/7/25.
 */

public  class CommonVolleyErrorListener implements Response.ErrorListener {
    private Context context;
    public CommonVolleyErrorListener(Context context){
        this.context=context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(context!=null){
            Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
        }
    }
}