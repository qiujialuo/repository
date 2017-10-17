package com.newcoder.toutiao.Service;

import com.newcoder.toutiao.Dao.NewsDao;
import com.newcoder.toutiao.POJO.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nowcoder on 2016/6/26.
 */
@Service
public class ToutiaoService {
    @Autowired
    private NewsDao newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }
}
