package org.telegram.bot.core.views;

import org.springframework.stereotype.Component;
import org.telegram.bot.core.interfaces.IView;
import org.telegram.bot.core.utils.SendMessageUtil;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class StringView implements IView<String> {

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
