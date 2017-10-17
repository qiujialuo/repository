package com.newcoder.toutiao.Utils;

import com.newcoder.toutiao.POJO.User;
import org.springframework.stereotype.Component;

/**
 * Created by qiujl on 2017/6/2.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
