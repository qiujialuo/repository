package com.newcoder.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.newcoder.toutiao.Service.JedisService;
import com.newcoder.toutiao.Utils.JedisGetKey;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiujl on 2017/6/21.
 */
@Service
public class eventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(eventConsumer.class);
    @Autowired
    JedisService jedisService;

    ApplicationContext applicationContext;
    Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> eventHandlerMap = applicationContext.getBeansOfType(EventHandler.class);
        if (eventHandlerMap != null) {
            for (Map.Entry<String, EventHandler> handler : eventHandlerMap.entrySet()) {
                List<EventType> eventTypes = handler.getValue().getSupportEventTypes();
                for (EventType eventType : eventTypes) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<EventHandler>());
                    }
                    config.get(eventType).add(handler.getValue());
                }
            }
        }

        // 启动线程去消费事件
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = JedisGetKey.getEventQueueKey();
                    List<String> list = jedisService.brpop(0, key);
                    System.out.println("ABD");
                    for (String str : list) {
                        if(str.equals("EVENT")){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(str, EventModel.class);

                        // 找到这个事件的处理handler列表
                        if (!config.containsKey(eventModel.getEventType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler eventHandler : config.get(eventModel.getEventType())) {
                            eventHandler.doHandler(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
