package io.github.aliaksandrrachko.telegram.bot.exception;

import lombok.Getter;

public class UnsupportedViewException extends RuntimeException {

    private static final String MESSAGE = "Not found supported view by '%s'";

    @Getter
    private final Class<?> clazz;

    public UnsupportedViewException(Class<?> clazz) {
        super(String.format(MESSAGE, clazz.getName()));
        this.clazz = clazz;
    }
}
