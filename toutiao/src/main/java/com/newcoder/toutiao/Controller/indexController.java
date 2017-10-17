package com.newcoder.toutiao.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by qiujl on 2017/5/27.
 */
@Controller
public class indexController {

    @RequestMapping("/profile/{groupId}/{userId}")
    @ResponseBody       //将return的String或者别的对象Object给当前url的页面
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") String userId,
                          @RequestParam(value = "type",defaultValue = "1") int type,
                          @RequestParam(value = "key",defaultValue = "1") int key ){
        return String.format("GID{%s},UID{%s},T{%d},key{%d}",groupId,userId,type,key);
    }
    @RequestMapping("/vm")
    public String news(Model model){
        model.addAttribute("value","</br>shabi");
        List<String> list= Arrays.asList(new String[]{"RED","BLUE"});
        model.addAttribute("value2",list);

        Map<String,String> map=new HashMap<String, String>();
        for (int i=0;i<3;++i){
            map.put(i+"",i*i+"");
        }
        model.addAttribute("value3map",map);
        return "news";
    }



    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request, HttpServletResponse response ){

        String str=request.getMethod();
        StringBuffer sb=new StringBuffer();
        Enumeration<String> headernames=request.getHeaderNames();
        while(headernames.hasMoreElements()){
            String name=headernames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"</br>");
        }
        return sb.toString();
    }
}
