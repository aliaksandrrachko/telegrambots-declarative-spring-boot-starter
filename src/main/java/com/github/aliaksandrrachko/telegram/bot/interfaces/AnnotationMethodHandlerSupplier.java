package com.github.aliaksandrrachko.telegram.bot.interfaces;

import com.github.aliaksandrrachko.telegram.bot.Operation;

import java.lang.reflect.Method;

public interface AnnotationMethodHandlerSupplier {

    Method getSupportedMethod(Operation operation, String... value);
    Method getSupportedExceptionHandler(Class<? extends Throwable>... value);
    Object getOriginalObject(Method method);
}
