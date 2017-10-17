package com.newcoder.toutiao;

import com.newcoder.toutiao.Dao.CommentDao;
import com.newcoder.toutiao.Dao.LoginTicketDao;
import com.newcoder.toutiao.Dao.UserDao;
import com.newcoder.toutiao.Dao.NewsDao;
import com.newcoder.toutiao.POJO.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

/**
 * Created by qiujl on 2017/5/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
//@WebAppConfiguration  这行会修改默认的启动路径需要注释掉
@Sql({"/init-scheema.sql"})
public class initDatabaseTests {

    @Autowired
    NewsDao newsDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CommentDao commentDao;

    @Autowired
    LoginTicketDao loginTicketDao;

    @Test
    public void InitDataBase() {
        Random r = new Random();
        News news = new News();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setName(String.format("USER%d", i));
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", r.nextInt(1000)));
            user.setPassword("");
            user.setSalt("");
            userDao.addUser(user);

            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", r.nextInt(1000)));
            news.setLikeCount(0);
            news.setLink(String.format("http://www.nowcoder.com/link/{%d}.html", i));
            news.setTitle(String.format("Title {%d} ", i));
            news.setUserId(i+1);
            newsDao.addNews(news);
            System.out.println(news.getId());
            // 给每个资讯插入3个评论
            for(int j = 0; j < 3; ++j) {
                Comment comment = new Comment();
                comment.setUserId(i+1);
                comment.setCreatedDate(new Date());
                comment.setStatus(0);
//                comment.setContent("这里是一个评论啊！" + String.valueOf(j));
                comment.setContent("qqq" + String.valueOf(j));
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ABOUT_NEWS);
                commentDao.insertComment(comment);
                newsDao.updateNews(j+1,i+1);
            }
            user.setPassword("newpassword");
            userDao.updatePassword(user);

            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d", i+1));
            loginTicketDao.addTicket(ticket);

            loginTicketDao.updateStatus(ticket.getTicket(), 2);
        }

        Assert.assertEquals("newpassword", userDao.selectById(1).getPassword());
        userDao.deleteById(1);
        Assert.assertNull(userDao.selectById(1));
    }
}
