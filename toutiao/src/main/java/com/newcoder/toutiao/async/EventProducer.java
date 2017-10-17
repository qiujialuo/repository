package com.newcoder.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import com.newcoder.toutiao.Service.JedisService;
import com.newcoder.toutiao.Utils.JedisGetKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qiujl on 2017/6/21.
 */
@Service
public class EventProducer {
    @Autowired
    JedisService jedisService;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String value = JSONObject.toJSONString(eventModel);
            String eventKey = JedisGetKey.getEventQueueKey();
            jedisService.lpush(eventKey,value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
