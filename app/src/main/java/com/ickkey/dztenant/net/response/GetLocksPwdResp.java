package com.ickkey.dztenant.net.response;

import java.io.Serializable;

/**
 * Created by hhj on 2017/8/16.
 */

public class GetLocksPwdResp implements Serializable {
    public Pwd msg;
    public class Pwd implements Serializable{
        public String pwd;
        public String pwdId;
        public int locksId;
    }
}
