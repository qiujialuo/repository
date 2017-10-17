package com.newcoder.toutiao.Controller;

import com.newcoder.toutiao.POJO.EntityType;
import com.newcoder.toutiao.POJO.News;
import com.newcoder.toutiao.POJO.User;
import com.newcoder.toutiao.Service.JedisService;
import com.newcoder.toutiao.Service.NewsService;
import com.newcoder.toutiao.Utils.HostHolder;
import com.newcoder.toutiao.Utils.ToutiaoUtils;
import com.newcoder.toutiao.async.EventModel;
import com.newcoder.toutiao.async.EventProducer;
import com.newcoder.toutiao.async.EventType;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by qiujl on 2017/6/16.
 */
@Controller
public class JedisController {

    @Autowired
    JedisService jedisService;

    @Autowired
    NewsService newsService;

    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;


    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newId") int newsId) {
        User user = hostHolder.getUser();
        if (user != null) {
            long likeCount = jedisService.like(user.getId(), EntityType.ABOUT_NEWS, newsId);
            newsService.uopdateNewsLike((int) likeCount, newsId);
            EventModel eventModel=new EventModel();
            eventModel.setEntityId(newsId).setEntityType(EntityType.ABOUT_NEWS)
                    .setActorId(user.getId())
                    .setEntityOwnerId(newsService.getNews(newsId).getUserId())
            .setEventType(EventType.Like);
            eventProducer.fireEvent(eventModel);
            return ToutiaoUtils.getJSONString(0, String.valueOf(likeCount));
        } else {
            int likeCount = newsService.getNews(newsId).getLikeCount();
            return ToutiaoUtils.getJSONString(0, String.valueOf(likeCount));
        }
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newId") int newsId) {
        User user = hostHolder.getUser();
        if (user != null) {
            News news = newsService.getNews(newsId);
            long likeCount = jedisService.dislike(user.getId(), EntityType.ABOUT_NEWS, newsId);

            newsService.uopdateNewsLike((int) likeCount, newsId);
            return ToutiaoUtils.getJSONString(0, String.valueOf(likeCount));
        } else {
            int likeCount = newsService.getNews(newsId).getLikeCount();
            return ToutiaoUtils.getJSONString(0, String.valueOf(likeCount));
        }
    }
}
