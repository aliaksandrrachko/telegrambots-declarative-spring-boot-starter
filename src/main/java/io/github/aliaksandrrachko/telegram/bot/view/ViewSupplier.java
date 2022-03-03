package io.github.aliaksandrrachko.telegram.bot.view;

import java.lang.reflect.Method;

public interface ViewSupplier {

    <T> View<T> get(T methodResult, Method m);
}
