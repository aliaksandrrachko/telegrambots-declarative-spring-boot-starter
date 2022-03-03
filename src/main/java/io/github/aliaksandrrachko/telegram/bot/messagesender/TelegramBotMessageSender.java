package io.github.aliaksandrrachko.telegram.bot.messagesender;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.List;

public interface TelegramBotMessageSender {

    void sendMessage(SendMessage sendMessage);
    void sendMessage(List<PartialBotApiMethod<? extends Serializable>> messagesToSend);
}
