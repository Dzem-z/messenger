package com.project.messenger.helpers.debugging;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ControllerLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();

            // Store or log the controller and method
            System.out.println("Request handled by: " + controllerName + "#" + methodName);
        } else {
            System.out.println("Handler is not a controller method." +  handler.getClass().getName());
        }
        return true;
    }
}