package com.ickkey.dztenant.event;

import com.ickkey.dztenant.net.response.GetLocksResp;

import java.io.Serializable;

/**
 * Created by hhj on 2017/8/28.
 */

public class LockRefreshEvent implements Serializable {
    public GetLocksResp.LockItem lockItem;
}
