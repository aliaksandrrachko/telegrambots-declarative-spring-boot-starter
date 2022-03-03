package io.github.aliaksandrrachko.telegram.bot.views;

import io.github.aliaksandrrachko.telegram.bot.interfaces.View;
import io.github.aliaksandrrachko.telegram.bot.utils.SendMessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class StringView implements View<String> {

    @Override
    public Class<String> getGenericType() {
        return String.class;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String chatId) {
        List<PartialBotApiMethod<? extends Serializable>> sendMessages = new ArrayList<>();
        SendMessage messageTemplate = SendMessageUtil.createMessageTemplate(chatId);
        messageTemplate.setText((String) entity);
        sendMessages.add(messageTemplate);
        return sendMessages;
    }

    @Override
    public boolean supports(Class<?> entityClass) {
        return entityClass.equals(String.class);
    }
}
