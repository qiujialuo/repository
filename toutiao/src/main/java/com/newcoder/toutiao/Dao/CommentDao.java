package com.newcoder.toutiao.Dao;

import com.newcoder.toutiao.POJO.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by qiujl on 2017/6/9.
 */
@Mapper
public interface CommentDao {

    String TABLE_NAME = " comment ";
    String SELECT_VALUE = "( content, entity_id, entity_type, created_date, user_id, status";

    @Select({"Select * from", TABLE_NAME, "where entity_id = #{entityId} and entity_type = #{entityType} order by id desc"})
    public List<Comment> selectComment(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Insert({"Insert into", TABLE_NAME, SELECT_VALUE, ") values (#{content},#{entityId},#{entityType},#{createdDate},#{userId},#{status})"})
    public int insertComment(Comment comment);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId}"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);


}