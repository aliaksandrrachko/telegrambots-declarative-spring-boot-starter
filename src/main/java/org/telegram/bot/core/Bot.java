package org.telegram.bot.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.bot.core.interfaces.TelegramBotMessageSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot implements TelegramBotMessageSender {

    @Value("${bot.name}")
    @Getter
    private String botUsername;

    @Value("${bot.token}")
    @Getter
    private String botToken;

    private final UpdateReceiver updateReceiver;

    public Bot(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }

    /**
     * The entry point where messages from users will go.
     * All new logic will come from here.
     *
     * @param update the object represents an incoming update.
     */
    @Override
    public void onUpdateReceived(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);

        extracted(messagesToSend);
    }

    public <T extends Serializable, M extends BotApiMethod<T>> void executeWithExceptionCheck(M sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("oops");
        }
    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        executeWithExceptionCheck(sendMessage);
    }

    @Override
    public void sendMessage(List<PartialBotApiMethod<? extends Serializable>> messagesToSend) {
        extracted(messagesToSend);
    }

    private void extracted(List<PartialBotApiMethod<? extends Serializable>> messagesToSend) {
        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage sendMessage) {
                    executeWithExceptionCheck(sendMessage);
                }
            });
        }
    }
}
