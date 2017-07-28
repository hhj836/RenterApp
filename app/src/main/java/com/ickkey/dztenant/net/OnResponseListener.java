package com.ickkey.dztenant.net;

/**
 * Created by Administrator on 2017/7/25.
 */

public interface OnResponseListener<T> {
    void  onSucceed(T t);
    void  onError(String errorMsg);

}
