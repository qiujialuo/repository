package com.newcoder.toutiao.Service;


import com.newcoder.toutiao.Dao.LoginTicketDao;
import com.newcoder.toutiao.Dao.UserDao;
import com.newcoder.toutiao.POJO.LoginTicket;
import com.newcoder.toutiao.POJO.User;
import com.newcoder.toutiao.Utils.ToutiaoUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by qiujl on 2017/5/30.
 */
@Service
public class UserService {
    @Autowired
    UserDao userdao;

    @Autowired
    LoginTicketDao loginTicketDao;

    public User getUser(int id) {
        return userdao.selectById(id);
    }

    public User getUserByName(String name) {
        return userdao.selectByName(name);
    }
    public Map<String,Object> register(String name, String password){
        Map<String,Object > map=new HashMap<String,Object>();

        if(StringUtils.isBlank(name)){
            map.put("msgname","用户名不能为空");
            return map;
        }else if(StringUtils.isBlank(password)){
            map.put("msgpsw","密码不能为空");
            return map;
        }

        //判断username是否被注册了
        if(userdao.selectByName(name)!=null){
            map.put("msgname","该用户已经被注册");
            return map;
        }

        //注册
        User user=new User();
        user.setName(name);
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        //进行salt加密
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        password=password+user.getSalt();
        user.setPassword(ToutiaoUtils.MD5(password));
        userdao.addUser(user);

        // 登陆
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String,Object> login(String name, String password){
        Map<String,Object > map=new HashMap<String,Object>();

        if(StringUtils.isBlank(name)){
            map.put("msgname","用户名不能为空");
            return map;
        }else if(StringUtils.isBlank(password)){
            map.put("msgpsw","密码不能为空");
            return map;
        }
        User user=new User();
        user=userdao.selectByName(name);
        if(user==null){
            map.put("msgnuser","该用户不存在");
            return map;
        }else if(!ToutiaoUtils.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpsw","该密码不正确");
            return map;
        }
        // 登陆
        if(loginTicketDao.selectStatusByuserId(user.getId())!=null){
            loginTicketDao.updateStatusByuserId(user.getId());
        }
        String ticket = addLoginTicket(user.getId());
        map.put("user",user);
        map.put("ticket", ticket);
        return map;
    }

    private String addLoginTicket(int id) {

        LoginTicket loginticket = new LoginTicket();
        loginticket.setUserId(id);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        loginticket.setExpired(date);
        loginticket.setStatus(0);//0为有效

        //设置ticket
        loginticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.addTicket(loginticket);

        return loginticket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket,1);
    }
}
