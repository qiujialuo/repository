package com.newcoder.toutiao.Dao;


import com.newcoder.toutiao.POJO.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by rainday on 16/6/30.
 */
@Mapper
public interface NewsDao {

    String TABLE_NAME = "news";

    String INSERT_FIELDS = " title, link, image, like_count, comment_count,created_date,user_id ";

    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,") Values (#{title},#{link},#{image},#{likeCount}, #{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    News selectById(int id);

    @Update({"update ", TABLE_NAME, "set comment_count = #{count} where id=#{id}"})
    int updateNews(@Param("count") int count ,@Param("id") int id);

    @Update({"update ", TABLE_NAME, "set like_count = #{likeCount} where id=#{id}"})
    int updateNewsLike(@Param("likeCount") int likeCount ,@Param("id") int id);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}
