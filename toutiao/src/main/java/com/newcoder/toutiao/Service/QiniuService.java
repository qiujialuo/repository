package com.newcoder.toutiao.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by qiujl on 2017/6/3.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    //构造一个带指定Zone对象的配置类
    Configuration cfg = new Configuration(Zone.zone2());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    String accessKey = "2LpD64GPkHqfRgmpwypMSNeCjx-YFTV3Mw8DKUX5";
    String secretKey = "czLDuJDbiGmFkTxjJgTh7OrywVoJo0-2dvKZaRlb";
    String bucket = "toutiao";
    //默认不指定key的情况下，以文件内容的hash值作为文件名
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);

    private static String QINIU_IMAGE_DOMAIN = "http://oqymqxypg.bkt.clouddn.com/";
    public String uploadImage(MultipartFile file) throws IOException {
        int pos=file.getOriginalFilename().lastIndexOf('.');
        if(pos<0){
            return null;
        }
        String fileExt=file.getOriginalFilename().substring(pos+1);//取文件的后缀名
        String fileName= UUID.randomUUID().toString().replaceAll("-","").toString()+"."+fileExt;
        try {
            //调用put方法上传
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            if (response.isOK() && response.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(response.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + response.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            logger.error("七牛异常:" + e.getMessage());
            return null;
        }
    }
}