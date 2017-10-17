package com.newcoder.toutiao.async;

import com.newcoder.toutiao.async.EventModel;
import com.newcoder.toutiao.async.EventType;

import java.util.List;

/**
 * Created by qiujl on 2017/6/21.
 */
public interface EventHandler {
    void doHandler(EventModel eventModel);
    List<EventType> getSupportEventTypes();
}
