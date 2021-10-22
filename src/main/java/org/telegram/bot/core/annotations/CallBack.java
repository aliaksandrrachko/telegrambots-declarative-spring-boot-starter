package org.telegram.bot.core.annotations;


import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CallBackQuery it's query from bot, so use message.getChatId(),
 * in another methods use message.getFrom().getId().
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CallBack {

    String[] value() default {};

    String regexp() default "";
}
