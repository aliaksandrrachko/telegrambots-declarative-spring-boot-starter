package io.github.aliaksandrrachko.telegram.bot.method;

import java.lang.reflect.Method;

public interface MethodExecutor {

    Object execute(Method method, Object... additionalArgs);
}
