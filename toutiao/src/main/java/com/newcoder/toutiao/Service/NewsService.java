package com.newcoder.toutiao.Service;

import com.newcoder.toutiao.Dao.NewsDao;
import com.newcoder.toutiao.POJO.News;
import com.newcoder.toutiao.Utils.ToutiaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/**
 * Created by qiujl on 2017/6/3.
 */
@Service
public class NewsService {

    @Autowired
    NewsDao newsDao;


    public String uploadImage(MultipartFile uploadImage)  throws IOException {
        //判断合法性
        int pos=uploadImage.getOriginalFilename().lastIndexOf('.');
        if(pos<0){
            return null;
        }
        String fileExt=uploadImage.getOriginalFilename().substring(pos+1);//取文件的后缀名
        String fileName= UUID.randomUUID().toString().replaceAll("-","").toString()+"."+fileExt;

        if(ToutiaoUtils.isFileAllowed(fileExt)){
            Files.copy(uploadImage.getInputStream(),new File(ToutiaoUtils.FILE_PATH+fileName).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            return ToutiaoUtils.TOUTIAO_DOMAIN+ "image?name=" + fileName;
        }
        return null;
    }


    public News getNews(int newId){
        return newsDao.selectById(newId);
    }

    public int uopdateNews(int count, int newsId){
        return newsDao.updateNews(count,newsId);
    }

    public int uopdateNewsLike(int likeCount, int newsId){
        return newsDao.updateNewsLike(likeCount,newsId);
    }
    public void addNews(News news) throws Exception{
        newsDao.addNews(news);
    }
}
