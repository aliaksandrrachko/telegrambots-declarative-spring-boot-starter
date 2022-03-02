package com.github.aliaksandrrachko.telegram.bot.interfaces;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

public interface View<T>{

    Class<T> getGenericType();

    List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String chatId);

    boolean supports(Class<?> entityClass);
}
