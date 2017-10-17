package com.newcoder.toutiao.Controller;


import com.newcoder.toutiao.POJO.User;
import com.newcoder.toutiao.Service.UserService;
import com.newcoder.toutiao.Utils.ToutiaoUtils;
import com.newcoder.toutiao.async.EventHandler;
import com.newcoder.toutiao.async.EventModel;
import com.newcoder.toutiao.async.EventProducer;
import com.newcoder.toutiao.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiujl on 2017/5/31.
 */
@Controller
public class loginController {
    private static final Logger logger = LoggerFactory.getLogger(loginController.class);
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(Model model, @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value="rember", defaultValue = "0") int rememberme,
                           HttpServletResponse response) {
                     //Map<String, Object> map = userService.register(username, password);
                    try {
                        Map<String, Object> map = userService.register(username, password);
                        if (map.containsKey("ticket")) {
                            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                            cookie.setPath("/");//所有路径下
                            if (rememberme > 0) {//最大存活天数
                                cookie.setMaxAge(3600 * 24 * 5);
                            }
                            response.addCookie(cookie);
                            return ToutiaoUtils.getJSONString(0, "注册成功");
                            }else {
                            return ToutiaoUtils.getJSONString(1, map);
                        }
                    }catch(Exception e){ logger.error("注册异常" + e.getMessage());
                        return ToutiaoUtils.getJSONString(1, "注册异常");
                    }
    }
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember", defaultValue = "0") int rememberme,HttpServletResponse response){
             Map<String,Object> map=new HashMap<String,Object>();
            map=userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {//最大存活天数
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel()
                        .setEventType(EventType.Login)
                        .setActorId(((User)map.get("user")).getId())
                        .setExts("username", "zj")
                        .setExts("to", "867650864@qq.com"));
                return ToutiaoUtils.getJSONString(0, map);
            }else{
                return ToutiaoUtils.getJSONString(1, map);
            }
    }

    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
