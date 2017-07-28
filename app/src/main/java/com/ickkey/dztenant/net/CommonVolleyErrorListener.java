package com.ickkey.dztenant.net;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ickkey.dztenant.utils.LogUtil;

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
        LogUtil.info(getClass(),error.getMessage());
        if(context!=null){
            Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
        }
    }
}