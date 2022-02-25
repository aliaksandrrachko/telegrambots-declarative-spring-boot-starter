package org.telegram.bot.core;

import lombok.Getter;
import org.telegram.bot.core.annotations.CallBack;
import org.telegram.bot.core.annotations.Command;
import org.telegram.bot.core.annotations.MessageTextQuery;
import org.telegram.bot.core.annotations.State;

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
