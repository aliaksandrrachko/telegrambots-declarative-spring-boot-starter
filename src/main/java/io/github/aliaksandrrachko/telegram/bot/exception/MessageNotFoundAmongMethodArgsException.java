package io.github.aliaksandrrachko.telegram.bot.exception;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;

public class MessageNotFoundAmongMethodArgsException extends RuntimeException{

    private static final String MESSAGE_PATTERNS = "%s argument is required. It's not found among %s";

    @Getter
    private final transient Object[] args;

    public MessageNotFoundAmongMethodArgsException(Object... methodArgs) {
        super(String.format(MESSAGE_PATTERNS, Message.class.getName(), Arrays.toString(methodArgs)));
        this.args = methodArgs;
    }
}
