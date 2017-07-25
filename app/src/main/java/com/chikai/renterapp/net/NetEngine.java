package com.chikai.renterapp.net;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.chikai.renterapp.net.response.BaseResponse;
import com.chikai.renterapp.net.response.TestResponseBean;

/**
 * Created by Administrator on 2017/7/25.
 */

public class NetEngine {
    /**
     * 测试网络请求
     */
    public static  void sendTestRequest(Context context, final OnResponseListener<TestResponseBean> onResponseListener, String tag){
        GsonRequest request=new GsonRequest("http://baidu.com", TestResponseBean.class,new Response.Listener<TestResponseBean>(){

            @Override
            public void onResponse(TestResponseBean response) {
                parseResponse(onResponseListener,response);
            }
        },new CommonVolleyErrorListener(context));
        HttpRequestUtils.addRequest(request,tag);
    }
    private static void parseResponse(OnResponseListener onResponseListener, BaseResponse baseResponse){
        if(baseResponse.code!=0&&!TextUtils.isEmpty(baseResponse.errorMsg)){
            onResponseListener.onError(baseResponse.errorMsg);

        }
        if(baseResponse.code==0){
            onResponseListener.onSucceed(baseResponse);
        }
    }

}
