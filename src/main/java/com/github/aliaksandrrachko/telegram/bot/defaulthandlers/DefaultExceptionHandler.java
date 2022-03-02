package com.github.aliaksandrrachko.telegram.bot.defaulthandlers;

import com.github.aliaksandrrachko.telegram.bot.annotations.Handler;
import com.github.aliaksandrrachko.telegram.bot.annotations.ExceptionHandler;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Handler
public class DefaultExceptionHandler {

    @ExceptionHandler
    public List<String> handleExceptionMessage(Message message, Throwable t) {
        String exceptionHappenedMessage = "Exception happened!\n";
        String throwableMessage = t.getMessage();
        List<String> result = new ArrayList<>();
        result.add(exceptionHappenedMessage);
        result.add(throwableMessage);
        return result;
    }
}
