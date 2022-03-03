package io.github.aliaksandrrachko.telegram.bot.interfaces;

import java.lang.reflect.Method;

public interface MethodExecutor {

    Object execute(Method method, Object... additionalArgs);
}
