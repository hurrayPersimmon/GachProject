package com.f2z.gach.Log;

import com.f2z.gach.EnumType.LogLevel;
import com.f2z.gach.EnumType.Properties;
import com.f2z.gach.Log.Entity.Log;
import com.f2z.gach.Log.Repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

@Aspect
@Component
@AllArgsConstructor
@Slf4j
public class MethodLoggerAspect {
    private final LogRepository logRepository;
    private static final Logger logger = LoggerFactory.getLogger(MethodLoggerAspect.class);
    @Around("execution(* com.f2z.gach.Map.Controller.*.*(..))")
    public Object logServiceController(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest httpRequest =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String requestPath = httpRequest.getRequestURI();
        if (httpRequest.getQueryString() != null) {
            requestPath += "?" + httpRequest.getQueryString();
        }

        long start = System.currentTimeMillis();

        logHttpRequest(httpRequest, requestPath);

        Object returnObj = joinPoint.proceed();

        long end = System.currentTimeMillis();

        logHttpResponse(httpRequest, returnObj);
        logHttp(httpRequest, requestPath, returnObj, start, end);

        return returnObj;
    }

    private void logHttpRequest(HttpServletRequest httpRequest, String requestPath) {
        logger.debug("HTTP Request [{}] {} from {}", httpRequest.getMethod(), requestPath, httpRequest.getRemoteAddr());
    }

    private void logHttpResponse(HttpServletRequest httpRequest, Object response) {
        HttpStatus responseStatus = (response instanceof ResponseEntity)
                ? (HttpStatus) ((ResponseEntity<?>) response).getStatusCode()
                : HttpStatus.OK;
        Object responseBody = (response instanceof ResponseEntity)
                ? ((ResponseEntity<?>) response).getBody()
                : response;

        if (responseBody != null) {
            logger.debug("HTTP Response [{} {}] to {} : {}", responseStatus.value(), responseStatus.name(), httpRequest.getRemoteAddr(), responseBody);
        } else {
            logger.debug("HTTP Response [{} {}] to {}", responseStatus.value(), responseStatus.name(), httpRequest.getRemoteAddr());
        }
    }

    private void logHttp(HttpServletRequest httpRequest, String requestPath, Object response, long start, long end) {
        HttpStatus responseStatus = (response instanceof ResponseEntity)
                ? (HttpStatus) ((ResponseEntity<?>) response).getStatusCode()
                : HttpStatus.OK;
        double gapSeconds = (double) (end - start) / 1000;
        String gapString = String.format("%.3f", gapSeconds);

        logger.info("{} [{}] {} from {} -> [{}] {} : {} seconds",
                httpRequest.getProtocol(),
                httpRequest.getMethod(),
                requestPath,
                httpRequest.getRemoteAddr(),
                responseStatus.value(),
                responseStatus.name(),
                gapString
        );

        Log logEntry = Log.builder()
                .logLevel(LogLevel.INFO)
                .httpMethod(HttpMethod.valueOf(httpRequest.getMethod()))
                .url(requestPath)
                .message("HTTP Request [" + httpRequest.getMethod() + "] " + requestPath + " from " + httpRequest.getRemoteAddr() + " -> [" + responseStatus.value() + "] " + responseStatus.name() + " : " + gapString + " seconds")
                .property(Properties.OK)
                .build();

        logRepository.save(logEntry);
    }
}

//    @Before("execution(* com.f2z.*.*Controller.*(..))")
//    public void logBeforeMethodCall(JoinPoint joinPoint) {
//        String methodName = joinPoint.getSignature().getName();
//        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
//        String httpMethod = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getMethod();
//        logger.info("Before calling method: {} with URI: {} using HTTP method: {}", methodName, uri, httpMethod);
//    }
//
//    @AfterReturning(pointcut = "execution(* com.f2z.*.*Controller.*(..))", returning = "result")
//    public void logAfterMethodReturn(JoinPoint joinPoint, Object result) {
//        String methodName = joinPoint.getSignature().getName();
//        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
//        String httpMethod = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getMethod();
//        Log log1 = getLogFromResponse(result);
//
//        logger.info("Method {} with URI {} using HTTP method {} returned: {}", methodName, uri, httpMethod, result);
//        log.info("Method {} with URI {} using HTTP method {} returned: {}", methodName, uri, httpMethod, result);
//        Log log = new Log();
//        log.setLogLevel(LogLevel.INFO);
////        log.setProperty("error_code");
//        log.setUserCode(123456L); // 사용자 코드 설정
//        log.setMessage("Method " + methodName + " with URI " + uri + " using HTTP method " + httpMethod + " returned: " + result);
//        log.setUrl(uri);
//        log.setHttpMethod(HttpMethod.valueOf(httpMethod)); // HTTP 메소드 설정
//
//        // Log 엔티티 저장
//        logRepository.save(log);
//    }
//
//    @AfterThrowing(pointcut = "execution(* com.f2z.*.*Controller.*(..))", throwing = "exception")
//    public void logAfterMethodException(JoinPoint joinPoint, Exception exception) {
//        String methodName = joinPoint.getSignature().getName();
//        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
//        String httpMethod = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getMethod();
//        logger.error("Method {} with URI {} using HTTP method {} threw exception: {}", methodName, uri, httpMethod, exception.getMessage());
//    }
//
//    private Log getLogFromResponse(Object result) {
//        log.info("Response: {}", result);
//        // 여기서 응답 데이터를 분석하여 사용자 ID를 추출하고 반환
//        return null;
//    }
//
//}
