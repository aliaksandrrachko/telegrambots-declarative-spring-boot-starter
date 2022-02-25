package org.telegram.bot.core.interfaces;

import java.lang.reflect.Method;

public interface ViewSupplier {

    <T> View<T> get(T methodResult, Method m);
}
