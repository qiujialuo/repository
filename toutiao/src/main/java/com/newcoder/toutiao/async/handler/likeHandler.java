package com.newcoder.toutiao.async.handler;

import com.newcoder.toutiao.POJO.Message;
import com.newcoder.toutiao.POJO.User;
import com.newcoder.toutiao.Service.MessageService;
import com.newcoder.toutiao.Service.NewsService;
import com.newcoder.toutiao.Service.UserService;
import com.newcoder.toutiao.Utils.HostHolder;
import com.newcoder.toutiao.async.EventHandler;
import com.newcoder.toutiao.async.EventModel;
import com.newcoder.toutiao.async.EventType;
import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by qiujl on 2017/6/21.
 */
@Service
public class likeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel eventModel) {
        System.out.println("LIKE");
        Message message= new Message();
//        message.setToId(eventModel.getEntityOwnerId());
        message.setToId(eventModel.getEntityOwnerId());
        message.setFromId(3);
        message.setCreatedDate(new Date());
        String str1=userService.getUser(eventModel.getActorId()).getName();
        String str2=String.valueOf(eventModel.getEntityId());
        message.setContent("用户" + str1+
                " 赞了你的资讯,http://127.0.0.1:8080/news/"
                + str2);
//        message.setConversationId();
        messageService.addConversion(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.Like);
    }
}
