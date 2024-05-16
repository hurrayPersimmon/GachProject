package com.f2z.gach.Log;

import com.f2z.gach.EnumType.LogLevel;
import com.f2z.gach.EnumType.Properties;
import com.f2z.gach.Log.Entity.Log;
import com.f2z.gach.Log.Repository.LogRepository;
import com.f2z.gach.Response.ResponseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@Aspect
@Component
@AllArgsConstructor
@Slf4j
public class MethodLoggerAspect {
    private final LogRepository logRepository;
    private static final Logger logger = LoggerFactory.getLogger(MethodLoggerAspect.class);

    @Before("execution(* com.f2z.*.*Controller.*(..))")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        String httpMethod = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getMethod();
        logger.info("Before calling method: {} with URI: {} using HTTP method: {}", methodName, uri, httpMethod);
    }

    @AfterReturning(pointcut = "execution(* com.f2z.*.*Controller.*(..))", returning = "result")
    public void logAfterMethodReturn(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        String httpMethod = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getMethod();
        Log log1 = getLogFromResponse(result);

        logger.info("Method {} with URI {} using HTTP method {} returned: {}", methodName, uri, httpMethod, result);
        log.info("Method {} with URI {} using HTTP method {} returned: {}", methodName, uri, httpMethod, result);
        Log log = new Log();
        log.setLogLevel(LogLevel.INFO);
//        log.setProperty("error_code");
        log.setUserCode(123456L); // 사용자 코드 설정
        log.setMessage("Method " + methodName + " with URI " + uri + " using HTTP method " + httpMethod + " returned: " + result);
        log.setUrl(uri);
        log.setHttpMethod(HttpMethod.valueOf(httpMethod)); // HTTP 메소드 설정

        // Log 엔티티 저장
        logRepository.save(log);
    }

    @AfterThrowing(pointcut = "execution(* com.f2z.*.*Controller.*(..))", throwing = "exception")
    public void logAfterMethodException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        String httpMethod = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getMethod();
        logger.error("Method {} with URI {} using HTTP method {} threw exception: {}", methodName, uri, httpMethod, exception.getMessage());
    }

    private Log getLogFromResponse(Object result) {
        log.info("Response: {}", result);
        // 여기서 응답 데이터를 분석하여 사용자 ID를 추출하고 반환
        return null;
    }

}
