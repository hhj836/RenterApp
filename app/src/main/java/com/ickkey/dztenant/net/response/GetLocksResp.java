package com.ickkey.dztenant.net.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hhj on 2017/8/15.
 */

public class GetLocksResp implements Serializable {
    public List<LockItem>  msg;
    public int code;
    public class LockItem implements Serializable{
        public int id;
        public String gatewayNo;
        public String pwd;
        public String gatewayValidNo;
        public String locksNo;
        public String locksValidNo;
        public String roomNo;
        public String province;
        public String city;
        public String district;
        public String installAddress;
        public String houseNumber;
        public String houseNo;
        public int isOnlie;
        public int quantity;
        public String createTime;
    }


}
