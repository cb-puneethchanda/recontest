package com.cb.reconciliation.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... args) {
        System.out.println(
                "[ERROR]\n"
                + "\nError Message: " + ex.getMessage()
                + "\nMethod Name" + method.getName()
                + "\nArgs:" + Arrays.toString(args));

    }
}
