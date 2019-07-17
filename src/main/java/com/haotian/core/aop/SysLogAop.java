package com.haotian.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SysLogAop {

    Logger looger = LoggerFactory.getLogger(SysLogAop.class);



    @Around("@annotation(com.haotian.core.aop.annotation.SysLogByInsert)")
    public Boolean aopForInsert(ProceedingJoinPoint point) throws Throwable {
        looger.info("aop for insert in");
        return (Boolean) point.proceed();
    }
    @Around("@annotation(com.haotian.core.aop.annotation.SysLogByDelete)")
    public void aopForDelete(ProceedingJoinPoint point) throws Throwable {
        System.out.println("aop for delete in ");
        point.proceed();
        System.out.println("aop for delete out ");
    }
    @Around("@annotation(com.haotian.core.aop.annotation.SysLogByUpdate)")
    public void aopForUpdate(ProceedingJoinPoint point) throws Throwable {
        System.out.println("aop for update in ");
        point.proceed();
        System.out.println("aop for update out ");
    }





}
