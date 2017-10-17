package com.newcoder.toutiao.async.handler;

import com.newcoder.toutiao.POJO.Message;
import com.newcoder.toutiao.Service.MessageService;
import com.newcoder.toutiao.Utils.MailSender;
import com.newcoder.toutiao.async.EventHandler;
import com.newcoder.toutiao.async.EventModel;
import com.newcoder.toutiao.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by qiujl on 2017/6/22.
 */
@Component
public class mailHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;
    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        message.setToId(eventModel.getActorId());
        message.setContent("你上次的登陆IP异常");
        // SYSTEM ACCOUNT
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addConversion(message);

        Map<String, Object> map = new HashMap();
        map.put("username", eventModel.getExts("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExts("to"), "登陆异常",
                "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.Login);
    }
}
