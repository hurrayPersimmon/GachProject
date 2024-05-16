package com.f2z.gach.Log;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodLoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(MethodLoggerAspect.class);

    @Before("execution(* com.f2z.*.*(..))")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Before calling method: {}", methodName);
    }

    @AfterReturning(pointcut = "execution(* com.f2z.*.*(..))", returning = "result")
    public void logAfterMethodReturn(Object result) {
        logger.info("Method returned: {}", result);
    }

    @AfterThrowing(pointcut = "execution(* com.f2z.*.*(..))", throwing = "exception")
    public void logAfterMethodException(Exception exception) {
        logger.error("Method threw exception: {}", exception.getMessage());
    }
}
