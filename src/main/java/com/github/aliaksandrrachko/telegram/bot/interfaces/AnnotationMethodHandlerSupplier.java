package com.github.aliaksandrrachko.telegram.bot.interfaces;

import com.github.aliaksandrrachko.telegram.bot.Operation;

import java.lang.reflect.Method;
import java.util.Map;

public interface AnnotationMethodHandlerSupplier {

    Map<String, Object> getAllHandlersBean();
    Method getSupportedMethod(Operation operation, String... value);
    Method getSupportedExceptionHandler(Class<? extends Throwable>... value);
    Object getOriginalObject(Method method);
}
