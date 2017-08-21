package com.ickkey.dztenant.net.response;

/**
 * Created by Administrator on 2017/7/25.
 */

public class LoginResponse extends BaseResponse{
    public int expire;
    public String token;
    public String headUrl;
    public String username;
    public String userId;
    public int isVisitor;
}
