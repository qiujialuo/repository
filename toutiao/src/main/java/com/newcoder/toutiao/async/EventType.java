package com.newcoder.toutiao.async;

/**
 * Created by qiujl on 2017/6/21.
 */
public enum EventType {
    Like(0),
    Dislike(1),
    Login(2);


    private int value;
    EventType() {

    }
    EventType(int value) {
        this.value = value;
    }
}
