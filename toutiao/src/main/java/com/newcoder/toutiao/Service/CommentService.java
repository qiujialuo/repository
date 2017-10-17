package com.newcoder.toutiao.Service;

import com.newcoder.toutiao.Dao.CommentDao;
import com.newcoder.toutiao.POJO.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qiujl on 2017/6/9.
 */
@Service
public class CommentService {

    @Autowired
    CommentDao commentDao;

    public void insertComment(Comment comment){
        commentDao.insertComment(comment);
    }

    public List<Comment> selectCommentEntity(int entityId,int entityType){
        return commentDao.selectComment(entityId,entityType);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }
}
