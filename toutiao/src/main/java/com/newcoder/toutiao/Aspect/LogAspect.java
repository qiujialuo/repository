package com.newcoder.toutiao.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by qiujl on 2017/5/29.
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.newcoder.toutiao.Controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuffer sb = new StringBuffer();
        for (Object arg : joinPoint.getArgs()) {
            sb.append("arg:" + arg.toString() + "|");
        }
        log.info("before method: " + sb.toString());
    }
}
