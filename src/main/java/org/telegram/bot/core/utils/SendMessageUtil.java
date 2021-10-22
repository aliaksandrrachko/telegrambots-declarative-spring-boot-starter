package org.telegram.bot.core.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@UtilityClass
public class SendMessageUtil {

    /**
     * Returns pattern SendMessage with enabled Markdown
     *
     * @param chatId the message chat id
     * @return the SendMessage with enabled Markdown
     */
    public SendMessage createMessageTemplate(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableMarkdown(true);
        return message;
    }

    public SendMessage createMessageTemplate(Long chatId) {
        return createMessageTemplate(String.valueOf(chatId));
    }
}
