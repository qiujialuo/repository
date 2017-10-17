package com.newcoder.toutiao.Controller;

import com.newcoder.toutiao.POJO.Message;
import com.newcoder.toutiao.POJO.User;
import com.newcoder.toutiao.POJO.ViewObject;
import com.newcoder.toutiao.Service.MessageService;
import com.newcoder.toutiao.Service.UserService;
import com.newcoder.toutiao.Utils.HostHolder;
import com.newcoder.toutiao.Utils.ToutiaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qiujl on 2017/6/9.
 */
@Controller
public class MessageController {
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;


    //  msg/msg-list?conversationId=10005_622873&updateRead=1&msgType=1
    @RequestMapping(value = {"msg/msg-list"}, method = {RequestMethod.GET})
    public String msgList(Model model, @RequestParam("conversationId") String conversationId,
                          @RequestParam("updateRead") int updateRead, @RequestParam("msgType") int msgType) {
        try {
            List<Message> conversationList = new ArrayList<Message>();
            conversationList  = messageService.getMessageByConversationId(conversationId, 0, 100);

            int userId=hostHolder.getUser().getId();
            messageService.update(conversationId,userId);

            List<ViewObject> list = new ArrayList<ViewObject>();
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                list.add(vo);
            }

            model.addAttribute("messages", list);
            return "letterDetail";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letterDetail";
    }


    @RequestMapping(value = {"msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model) {
        try {
            List<Message> list = new ArrayList<Message>();
            list = messageService.selectMessage(hostHolder.getUser().getId());
            List<ViewObject> vos = new ArrayList<ViewObject>();
            for (Message message : list) {
                ViewObject vo = new ViewObject();
                String conversation = message.getConversationId();
                int toId = message.getToId();
                int noRead = messageService.getMessageNoReadCount(toId, conversation);
                int allCount = messageService.getAllMessageCount(conversation);
                vo.set("noRead", noRead);
                vo.set("allCount", allCount);
                vo.set("msg", message);
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            return "letter";
        } catch (Exception e) {
            return ToutiaoUtils.getJSONString(1, "dfs");
        }
    }

    @RequestMapping(value = {"/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId, @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message msg = new Message();
            msg.setContent(content);
            msg.setCreatedDate(new Date());
            msg.setFromId(fromId);
            msg.setHasRead(0);
            msg.setToId(toId);
//            String conversationId = fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId);
//            msg.setConversationId(conversationId);
            messageService.addConversion(msg);
            return ToutiaoUtils.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("cjsld", e.getMessage());
            return ToutiaoUtils.getJSONString(1, "失败");
        }
    }
}
