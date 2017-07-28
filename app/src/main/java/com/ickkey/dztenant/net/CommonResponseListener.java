package com.ickkey.dztenant.net;

import android.text.TextUtils;
import android.widget.Toast;

import com.ickkey.dztenant.RenterApp;
import com.ickkey.dztenant.utils.ToastUtils;


/**
 * Created by Administrator on 2017/7/25.
 */

public class CommonResponseListener<T> implements OnResponseListener<T> {
    @Override
    public void onSucceed(T t) {

    }

    @Override
    public void onError(String errorMsg) {
        if(!TextUtils.isEmpty(errorMsg)){
            ToastUtils.showShortToast(RenterApp.getInstance(),errorMsg);
        }

    }
}
