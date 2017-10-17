package com.newcoder.toutiao.Configuration;

import com.newcoder.toutiao.Interceptor.LoginRequiredInterceptor;
import com.newcoder.toutiao.Interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by qiujl on 2017/6/2.
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*").addPathPatterns("/msg/*").addPathPatterns("/like").addPathPatterns("/dislike");;//只对url为setting*的进行拦截
        super.addInterceptors(registry);
    }
}

