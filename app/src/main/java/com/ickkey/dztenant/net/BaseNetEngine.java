package com.ickkey.dztenant.net;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.ickkey.dztenant.BuildConfig;
import com.ickkey.dztenant.R;
import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.net.request.BaseRequest;
import com.ickkey.dztenant.net.request.LoginReq;
import com.ickkey.dztenant.net.response.BaseResponse;
import com.ickkey.dztenant.net.response.LoginResponse;
import com.ickkey.dztenant.utils.DialogUtils;
import com.ickkey.dztenant.utils.Json2ObjHelper;
import com.ickkey.dztenant.utils.LogUtil;
import com.ickkey.dztenant.utils.NetUtil;
import com.ickkey.dztenant.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hhj
 * @date 2017/7/31
 * @Description:
 */


public class BaseNetEngine {
    public void getHttpResult(final OnResponseListener onResponseListener, final String url, final Class clazz, final Context context,BaseRequest...req){
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
                    requestUrl+=entry.getKey()+"="+((entry.getValue()instanceof String)? URLEncoder.encode((String) entry.getValue(),"utf-8"):entry.getValue())+"&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                LogUtil.info(getClass(),entry.getKey()+"="+entry.getValue());

            }
            requestUrl=requestUrl.substring(0,requestUrl.length()-1);
            LogUtil.info(NetEngine.class,"requestUrl=post="+requestUrl);

        }
        DialogUtils.showProgressDialog(context);
        new HttpThread(requestUrl,onResponseListener,clazz,context).start();
    }
    public class HttpThread extends Thread{
        public String url;
        public OnResponseListener onResponseListener;
        public Class clazz;
        public Context context;
        public HttpThread(String url,OnResponseListener onResponseListener,Class clazz,Context context){
            this.url=url;
            this.onResponseListener=onResponseListener;
            this.clazz=clazz;
            this.context=context;
        }
        @Override
        public void run() {
            String result=NetEngine.postDownloadJson(url,null);
            RenterApp.getInstance().getMainThreadHandler().post(new Runnable() {
                @Override
                public void run() {
                    DialogUtils.closeProgressDialog();
                }
            });
            if(!TextUtils.isEmpty(result)){
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getInt("code")!=0){

                        if(jsonObject.getInt("code")==501){
                            RenterApp.getInstance().getMainThreadHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    showTokenTimeoutDialog(context);
                                }
                            });
                        }else {
                            final String  error=jsonObject.getString("msg");
                            if(!TextUtils.isEmpty(error)){
                                RenterApp.getInstance().getMainThreadHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onResponseListener.onError(error);
                                    }
                                });
                            }
                        }

                    }else {
                        final Object resp=  Json2ObjHelper.getGson().fromJson(result,clazz);
                        if(resp!=null){
                            RenterApp.getInstance().getMainThreadHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    onResponseListener.onSucceed(resp);
                                }
                            });
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            LogUtil.info(getClass(),"result--"+result);
        }
    }
    public   <T extends BaseResponse> void sendPostRequest(final String url, final Context context, final Class<T> parseClass, final OnResponseListener<T> onResponseListener, final String tag, final BaseRequest...req){
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
                    requestUrl+=entry.getKey()+"="+((entry.getValue()instanceof String)? URLEncoder.encode((String) entry.getValue(),"utf-8"):entry.getValue())+"&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                LogUtil.info(getClass(),entry.getKey()+"="+entry.getValue());

            }
            requestUrl=requestUrl.substring(0,requestUrl.length()-1);
            LogUtil.info(NetEngine.class,"requestUrl=post="+requestUrl);

        }
        GsonRequest request=new GsonRequest(Request.Method.POST,requestUrl,parseClass,new Response.Listener<T>(){

            @Override
            public void onResponse(T response) {
                parseResponse(onResponseListener,response,context);
            }
        },new CommonVolleyErrorListener(context));
        HttpRequestUtils.getInstance().addRequest(request,tag);
    }

    public  <T extends BaseResponse> void sendGetRequest(String url, final Context context, Class<T> parseClass, final OnResponseListener<T> onResponseListener, String tag, BaseRequest...req){
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
                    requestUrl+=entry.getKey()+"="+((entry.getValue()instanceof String)? URLEncoder.encode((String) entry.getValue(),"utf-8"):entry.getValue())+"&";
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
                parseResponse(onResponseListener,response,context);
            }
        },new CommonVolleyErrorListener(context));
        HttpRequestUtils.getInstance().addRequest(request,tag);

    }
    private  <T extends BaseResponse> void parseResponse(OnResponseListener onResponseListener, T response, final Context context){
        if(response.code!=0){
            onResponseListener.onError(response.msg);

        }else {
            if(response.code==501){
                //token 过期
                showTokenTimeoutDialog(context);
            }else {
                onResponseListener.onSucceed(response);
            }

        }

    }
    public void showTokenTimeoutDialog(final Context context){
        AlertDialog dialog= DialogUtils.showDialog((Activity) context, R.layout.dialog_token_timeout, false, new DialogUtils.CustomizeAction() {
            @Override
            public void setCustomizeAction(final AlertDialog dialog, View view) {
                TextView btn_confirm= (TextView) view.findViewById(R.id.btn_confirm);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginReq loginReq=new LoginReq();
                        loginReq.mobile=!TextUtils.isEmpty(RenterApp.getInstance().getUserInfo().mobile)?RenterApp.getInstance().getUserInfo().mobile:RenterApp.getInstance().getUserInfo().username;
                        loginReq.password=!TextUtils.isEmpty(RenterApp.getInstance().getUserInfo().pwd)?RenterApp.getInstance().getUserInfo().pwd:RenterApp.getInstance().getPwd();
                        NetEngine.getInstance().sendLoginRequest(context,new CommonResponseListener<LoginResponse>(){
                            @Override
                            public void onSucceed(LoginResponse loginResponse) {
                                super.onSucceed(loginResponse);
                                loginResponse.tokenTimeOut=String.valueOf(System.currentTimeMillis()+loginResponse.expire*1000);
                                RenterApp.getInstance().saveUserInfo(loginResponse);
                                ToastUtils.showShortToast(context,"重登成功");
                                dialog.dismiss();

                            }
                        },null,loginReq);
                    }
                });

            }
        });
             /*   dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK)
                            return true;

                        return false;
                    }
                });*/
    }
    private  void reflect(final Class clazz, final Map<String,Object> params,
                        final Object requestObj) {
        if (clazz == null || params == null) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equals("shadow$_monitor_")||field.getName().equals("shadow$_klass_")){
                continue;
            }
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
    public static String postDownloadJson(String path,String post){
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            if(post!=null){
                PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
                // 发送请求参数
                printWriter.write(post);//post的参数 xx=xx&yy=yy
                // flush输出流的缓冲
                printWriter.flush();
            }

            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while((len=bis.read(arr))!= -1){
                bos.write(arr,0,len);
                bos.flush();
            }
            bos.close();
            return bos.toString("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
