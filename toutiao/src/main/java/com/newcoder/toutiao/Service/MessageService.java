package com.newcoder.toutiao.Service;

import com.newcoder.toutiao.Dao.MessageDao;
import com.newcoder.toutiao.POJO.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qiujl on 2017/6/10.
 */
@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;

    public List<Message> selectMessage(int userId){
        return messageDao.getConversationList(userId);
    }

    public void addConversion(Message message){
        messageDao.addMessage(message);
    }

    public int getMessageNoReadCount(int userId,String conversationId){
        return messageDao.getMessageCount(userId,conversationId);
    }

    public int getAllMessageCount(String conversationId){
        return messageDao.getAllMessageCount(conversationId);
    }

    public List<Message> getMessageByConversationId(String conversation,int start,int end){
        return messageDao.getMessageForConversationId(conversation,start,end);
    }

    public void update(String conversationId,int userId){
        messageDao.updateMessage(conversationId,userId);
    }
}
