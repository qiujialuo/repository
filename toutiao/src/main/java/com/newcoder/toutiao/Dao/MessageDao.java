package com.newcoder.toutiao.Dao;

import com.newcoder.toutiao.POJO.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by qiujl on 2017/6/10.
 */
@Mapper
public interface MessageDao {

    String TABLE_NAME = "message";
    String SELECT_VALUE = "( from_id,to_id,content,created_date,has_read,conversation_id";

    @Insert({"insert into ", TABLE_NAME, SELECT_VALUE, ")", "Values (#{fromId}, #{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})"})
    int addMessage(Message message);

    @Select({"SELECT a.* FROM message a  RIGHT JOIN (SELECT MAX(id) AS id FROM message WHERE from_id=#{userId} OR to_id=#{userId} GROUP BY conversation_id) tt ON a.id=tt.id ORDER BY created_date DESC"})
    List<Message> getConversationList(int userId);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getMessageCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select count(id) from ", TABLE_NAME, " where conversation_id=#{conversationId}"})
    int getAllMessageCount(String conversationId);

    @Select({"select * from ", TABLE_NAME, " where conversation_id=#{conversationId} order by id desc limit #{start},#{end}"})
    List<Message> getMessageForConversationId(@Param("conversationId") String conversationId, @Param("start") int start, @Param("end") int end);

    @Update({"update ", TABLE_NAME, "set has_read=1 where conversation_id=#{conversationId} and to_id=#{userId}"})
    int updateMessage(@Param("conversationId") String conversation, @Param("userId") int userId);
}
