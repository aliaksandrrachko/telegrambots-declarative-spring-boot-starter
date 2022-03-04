package io.github.aliaksandrrachko.telegram.bot.view;

import java.lang.reflect.Type;

public interface ViewSupplier {

    <T> View<T> get(T methodResult, Type type);
    <T> View<T> get(Class<?> clazz);
}
