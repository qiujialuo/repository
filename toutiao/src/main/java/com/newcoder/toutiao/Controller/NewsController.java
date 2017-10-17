package com.newcoder.toutiao.Controller;

import com.newcoder.toutiao.POJO.*;
import com.newcoder.toutiao.Service.CommentService;
import com.newcoder.toutiao.Service.NewsService;
import com.newcoder.toutiao.Service.QiniuService;
import com.newcoder.toutiao.Service.UserService;
import com.newcoder.toutiao.Utils.HostHolder;
import com.newcoder.toutiao.Utils.ToutiaoUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qiujl on 2017/6/3.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    QiniuService qiniuService;

    @RequestMapping(value = {"/addComment"}, method = { RequestMethod.POST})
    public String addComment(@RequestParam("newsId")int newsId,
                             @RequestParam("content") String content){

        try{
            Comment comment=new Comment();

            comment.setUserId(hostHolder.getUser().getId());
            comment.setCreatedDate(new Date());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ABOUT_NEWS);
            comment.setStatus(0);
            commentService.insertComment(comment);

            int count=commentService.getCommentCount(newsId,EntityType.ABOUT_NEWS);
            newsService.uopdateNews(count,newsId);
        }catch(Exception e){
            logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

    @RequestMapping(value = {"/news/{newsId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String comment(Model model, @PathVariable("newsId") int newsId) {
        List<News> list = new ArrayList<News>();
        try {
            News news = newsService.getNews(newsId);
            if (news != null) {
                List<Comment> comments = commentService.selectCommentEntity(newsId, EntityType.ABOUT_NEWS);
                List<ViewObject> commentVOs = new ArrayList<ViewObject>();
                for (Comment comment : comments) {
                    ViewObject vo=new ViewObject();
                    vo.set("comment",comment);
                    vo.set("user",userService.getUser(comment.getUserId()));
                    commentVOs.add(vo);
                }
                model.addAttribute("comments",commentVOs);
            }
            model.addAttribute("news", news);
            model.addAttribute("owner", userService.getUser(news.getUserId()));
        } catch (Exception e) {
            logger.error("获取资讯失败" + e.getMessage());
        }
        return "detail";
    }

    @RequestMapping(value = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String News(@RequestParam("image") String image,
                       @RequestParam("title") String title,
                       @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setImage(image);
            news.setLink(link);
            news.setTitle(title);
            news.setCreatedDate(new Date());
            if (hostHolder.getUser() != null) {
                User user = hostHolder.getUser();
                news.setUserId(user.getId());
            } else {
                news.setUserId(0);
            }
            newsService.addNews(news);
            return ToutiaoUtils.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtils.getJSONString(1, "发布失败");
        }
    }

    @RequestMapping(value = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@Param("file") MultipartFile file) {
        try {
            String fileUrl = newsService.uploadImage(file);
//            String fileUrl = qiniuService.uploadImage(file);
            if (fileUrl == null) {
                return ToutiaoUtils.getJSONString(1, "上传图片失败");
            }
            return ToutiaoUtils.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtils.getJSONString(1, "上传图片失败");
        }
    }


    @RequestMapping(value = {"/image"}, method = {RequestMethod.GET})
    public void image(@Param("name") String name, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtils.FILE_PATH + name)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("下载失败" + e.getMessage());
        }
    }
}
