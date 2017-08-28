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
        String EXIST_MOBILE=BASE_URL+"/api/existMobile";
        String GET_LOCK_PWD=BASE_URL+"/api/getlockspwd";
        String UPDATE_LOCK_PWD=BASE_URL+"/api/updatelockspwd";
        String SEARCH_LOCK=BASE_URL+"/api/searchlocks";
        String GET_LOCKS_ID=BASE_URL+"/api/getlocksId";
        String GET_APP_VERSION=BASE_URL+"/api/version";
        String GET_TEMP_PWD=BASE_URL+"/api/open/getTemp";

}
