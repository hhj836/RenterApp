package com.ickkey.dztenant.net.response;

import java.io.Serializable;

/**
 * Created by hhj on 2017/8/18.
 */

public class GetLocksIdResp implements Serializable {
    public int code;
    public GetLocksResp.LockItem msg;
}
