package io.github.aliaksandrrachko.telegram.bot.view;

import io.github.aliaksandrrachko.telegram.bot.utils.SendMessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class IntegerView implements View<Integer>{

    @Override
    public Class<Integer> getGenericType() {
        return Integer.class;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String chatId) {
        List<PartialBotApiMethod<? extends Serializable>> sendMessages = new ArrayList<>();
        SendMessage messageTemplate = SendMessageUtil.createMessageTemplate(chatId);
        messageTemplate.setText(String.valueOf(entity != null ? (int) entity : ""));
        sendMessages.add(messageTemplate);
        return sendMessages;
    }

    @Override
    public boolean supports(Class<?> entityClass) {
        return Integer.class.isAssignableFrom(entityClass);
    }
}
