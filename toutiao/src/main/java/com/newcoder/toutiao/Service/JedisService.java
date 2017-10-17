package com.newcoder.toutiao.Service;

import com.newcoder.toutiao.POJO.EntityType;
import com.newcoder.toutiao.POJO.News;
import com.newcoder.toutiao.Utils.JedisGetKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by qiujl on 2017/6/16.
 */
@Service
public class JedisService implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(JedisService.class);
    private JedisPool pool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);//项目启动就执行
    }

    public boolean lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lpush(key, value);
            return true;
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = JedisGetKey.getLikeKey(entityType, entityId);
        if (sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String disLikeKey = JedisGetKey.getDislikeKey(entityType, entityId);
        return sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    public long like(int userId, int entityType, int entityId) {
        String likeKey = JedisGetKey.getLikeKey(entityType, entityId);
        sadd(likeKey, String.valueOf(userId));
        String dislikeKey = JedisGetKey.getDislikeKey(entityType, entityId);
        srem(dislikeKey, String.valueOf(userId));
        return scard(likeKey);
    }

    public long dislike(int userId, int entityType, int entityId) {
        String likeKey = JedisGetKey.getLikeKey(entityType, entityId);
        srem(likeKey, String.valueOf(userId));
        String dislikeKey = JedisGetKey.getDislikeKey(entityType, entityId);
        sadd(dislikeKey, String.valueOf(userId));
        return scard(likeKey);
    }

    /**
     * Remove one or more members from a set
     *
     * @param key   集合名
     * @param value
     * @return
     */
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * Get the number of members in a set
     *
     * @param key 集合名
     * @return
     */
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * Add one or more members to a set
     *
     * @param key   集合名
     * @param value
     * @return
     */
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
