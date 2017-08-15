package com.ickkey.dztenant.net;

/**
 * Created by hhj on 2017/7/27.
 */

public interface Urls {
        String BASE_URL="http://ziru.ickkey.com";
        String LOGIN=BASE_URL+"/api/login";
        String GET_VERIFY=BASE_URL+"/api/verify";
        String REGISTER=BASE_URL+"/api/register";
        String UPPWD=BASE_URL+"/api/uppwd";
        String UPUSERNAME=BASE_URL+"/api/upusername";
        String FIND_PWD=BASE_URL+"/api/findpwd";
        String GET_LOCKS=BASE_URL+"/api/getlocks";
}
