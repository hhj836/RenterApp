package com.ickkey.dztenant.net;

import android.content.Context;
import android.text.TextUtils;

import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.net.request.BaseRequest;
import com.ickkey.dztenant.net.response.BaseResponse;
import com.ickkey.dztenant.net.response.GetVerifyResp;
import com.ickkey.dztenant.net.response.LoginResponse;
import com.ickkey.dztenant.net.response.SearchLocksResp;
import com.ickkey.dztenant.utils.DialogUtils;
import com.ickkey.dztenant.utils.Json2ObjHelper;
import com.ickkey.dztenant.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
    public void sendLoginRequest(Context context, final OnResponseListener<LoginResponse> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.LOGIN,context,LoginResponse.class,onResponseListener,tag,req);
    }

    /**
     * 获取验证码
     */
    public void sendGetVerifyRequest(Context context, final OnResponseListener<GetVerifyResp> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.GET_VERIFY,context,GetVerifyResp.class,onResponseListener,tag,req);
    }
    /**
     * 注册
     */
    public void sendRegisterRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.REGISTER,context,BaseResponse.class,onResponseListener,tag,req);
    }
    /**
     * 修改密码
     */
    public void sendUpdatePwdRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.UPPWD,context,BaseResponse.class,onResponseListener,tag,req);
    }
    /**
     * 修改用户名
     */
    public void sendUpdateUserNameRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.UPUSERNAME,context,BaseResponse.class,onResponseListener,tag,req);
    }
    /**
     * 找回密码
     */
    public void sendFindPwdRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.FIND_PWD,context,BaseResponse.class,onResponseListener,tag,req);
    }
    /**
     * 判断是否注册过
     */
    public void sendExistMobileRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.EXIST_MOBILE,context,BaseResponse.class,onResponseListener,tag,req);
    }
    /**
     * 设置锁密码
     */
    public void sendUpdateLocksPwdRequest(Context context, final OnResponseListener<BaseResponse> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.UPDATE_LOCK_PWD,context,BaseResponse.class,onResponseListener,tag,req);
    }
    /**
     * 搜索锁
     */
    public void sendSearchLocksRequest(Context context, final OnResponseListener<SearchLocksResp> onResponseListener, String tag, BaseRequest...req){
        sendPostRequest(Urls.SEARCH_LOCK,context,SearchLocksResp.class,onResponseListener,tag,req);
    }

}
