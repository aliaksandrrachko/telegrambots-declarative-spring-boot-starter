package com.github.aliaksandrrachko.telegram.bot;

import com.github.aliaksandrrachko.telegram.bot.annotations.MessageTextQuery;
import com.github.aliaksandrrachko.telegram.bot.annotations.State;
import lombok.Getter;
import com.github.aliaksandrrachko.telegram.bot.annotations.CallBack;
import com.github.aliaksandrrachko.telegram.bot.annotations.Command;

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
