package com.f2z.gach.Config;

import com.f2z.gach.EnumType.LogLevel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class InterceptorConfig implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(InterceptorConfig.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("InterceptorConfig preHandle");
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        LogLevel logLevel = LogLevel.INFO;

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("InterceptorConfig postHandle");
        log.info("response" + response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("InterceptorConfig afterCompletion");
    }
}
