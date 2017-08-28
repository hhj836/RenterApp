package com.ickkey.dztenant.net.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hhj on 2017/8/28.
 */

public class GetAppVersionResp implements Serializable {
    public int code;
    public Msg msg;
    public class Msg implements Serializable{
        public String androidUrl;
        public String androidVersion;
        public List<String> androidNotes;
    }

}
