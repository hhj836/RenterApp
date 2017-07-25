package com.chikai.renterapp.net;

import android.text.TextUtils;
import android.widget.Toast;

import com.chikai.renterapp.RenterApp;

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
            Toast.makeText(RenterApp.getInstance(),errorMsg,Toast.LENGTH_SHORT).show();
        }

    }
}
