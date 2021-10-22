package org.telegram.bot.core.interfaces;

import java.lang.reflect.Method;
import java.util.Map;

public interface IAnnotationMethodHandlerSupplier {

    Map<String, Object> getAllHandlersBean();
    Method getSupportedBotCommandMethod(String... value);
    Method getSupportedCallBackMethod(String... value);
    Method getSupportedMessageTextHandler(String... value);
    Method getSupportedStateHandler(String... value);
    Method getSupportedExceptionHandler(String... value);
    Object getOriginalObject(Method method);
}
