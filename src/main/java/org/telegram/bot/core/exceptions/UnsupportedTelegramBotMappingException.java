package org.telegram.bot.core.exceptions;

import lombok.Getter;

import java.util.Arrays;

public class UnsupportedTelegramBotMappingException extends RuntimeException{

    private static final String MESSAGE_PATTERN = "Not found handlers method for bot command '%s'";

    @Getter
    private final transient Object[] value;

    public UnsupportedTelegramBotMappingException(Object... value) {
        super(String.format(MESSAGE_PATTERN, Arrays.toString(value)));
        this.value = value;
    }
}
