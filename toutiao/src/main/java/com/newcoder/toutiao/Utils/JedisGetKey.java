package com.newcoder.toutiao.Utils;

/**
 * Created by qiujl on 2017/6/16.
 */
public class JedisGetKey {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    public static String getLikeKey(int entityTpye, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityTpye) + SPLIT + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityTpye, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityTpye) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }
}
