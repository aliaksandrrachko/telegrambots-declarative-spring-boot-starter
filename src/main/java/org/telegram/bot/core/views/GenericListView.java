package org.telegram.bot.core.views;

import org.telegram.bot.core.interfaces.IView;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenericListView implements IView<List<?>> {

    private final IView<?> iView;

    protected Class<?> clazz;

    public GenericListView(IView<?> iView, Class<?> clazz) {
        this.iView = iView;
        this.clazz = clazz;
    }

    @Override
    public Class<List<?>> getGenericType() {
        return null;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String chatId) {
        List<?> castedList = (List<?>) entity;
        List<PartialBotApiMethod<? extends Serializable>>  sendMessages = new ArrayList<>();
        castedList.forEach(e -> {
            List<PartialBotApiMethod<? extends Serializable>> render = iView.render(e, chatId);
            sendMessages.addAll(render);
        });
        return sendMessages;
    }

    @Override
    public boolean supports(Class<?> entityClass) {
        return entityClass != null && entityClass.isInstance(List.class);
    }
}
