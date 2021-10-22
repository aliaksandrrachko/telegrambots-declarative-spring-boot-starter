package org.telegram.bot.core.interfaces;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

public interface IView<T>{

    Class<T> getGenericType();

    List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String chatId);

    boolean supports(Class<?> entityClass);
}
