package org.telegram.bot.core.interfaces;

import java.lang.reflect.Method;

public interface MethodExecutor {

    Object execute(Method method, Object... additionalArgs);
}
