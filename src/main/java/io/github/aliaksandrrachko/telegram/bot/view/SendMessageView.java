package io.github.aliaksandrrachko.telegram.bot.view;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class SendMessageView implements View<SendMessage> {

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String chatId) {
        List<PartialBotApiMethod<? extends Serializable>> sendMessages = new ArrayList<>();
        sendMessages.add(getGenericType().cast(entity));
        return sendMessages;
    }

    @Override
    public Class<SendMessage> getGenericType() {
        return SendMessage.class;
    }

    @Override
    public boolean supports(Class<?> entityClass) {
        return entityClass.equals(SendMessage.class);
    }
}