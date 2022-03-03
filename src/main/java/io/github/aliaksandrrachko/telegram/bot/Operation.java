package io.github.aliaksandrrachko.telegram.bot;

import io.github.aliaksandrrachko.telegram.bot.annotation.MessageTextQuery;
import io.github.aliaksandrrachko.telegram.bot.annotation.State;
import lombok.Getter;
import io.github.aliaksandrrachko.telegram.bot.annotation.CallBack;
import io.github.aliaksandrrachko.telegram.bot.annotation.Command;

import java.lang.annotation.Annotation;

public enum Operation {

    CALL_BACK(CallBack.class),
    COMMAND(Command.class),
    STATE(State.class),
    MESSAGE_TEXT_HANDLER(MessageTextQuery.class);

    @Getter
    private final Class<? extends Annotation> annotationClass;

    Operation(Class<? extends Annotation> clazz) {
        this.annotationClass = clazz;
    }
}
