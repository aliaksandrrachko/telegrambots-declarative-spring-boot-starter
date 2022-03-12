package io.github.aliaksandrrachko.telegram.bot.view;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

public interface View<T>{

    Class<T> getGenericType();

    List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String id);

    boolean supports(Class<?> entityClass);
}
