package com.ickkey.dztenant.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ickkey.dztenant.utils.Json2ObjHelper;
import com.ickkey.dztenant.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/25.
 */

public class GsonRequest<T> extends Request<T> {

    private final Response.Listener<T> mListener;


    private Class<T> mClass;
    private Map<String,String> params;

    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener, Map<String,String>...params) {
        super(method, url, errorListener);
        mClass = clazz;
        mListener = listener;
        if(params.length>0){
            this.params=params[0];
        }

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if(params!=null){
            return params;
        }else {
            return super.getParams();
        }

    }
    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        this(Request.Method.GET, url, clazz, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            LogUtil.info(getClass(),"jsonString=="+jsonString);

            return Response.success(Json2ObjHelper.getGson().fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}