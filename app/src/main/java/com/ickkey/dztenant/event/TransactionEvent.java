package com.ickkey.dztenant.event;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * @author 侯慧杰
 * @date 2017/9/24
 * @Description:
 */


public class TransactionEvent {
    public static  final int TYPE_POP=0;
    public static  final int TYPE_START_AND_POP=1;
    public int eventType;

    public TransactionEvent(String tag) {
        this.tag = tag;
    }

    public String tag;
    public ISupportFragment toFragment;

}
