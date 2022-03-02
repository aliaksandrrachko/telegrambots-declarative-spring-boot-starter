package com.github.aliaksandrrachko.telegram.bot.annotations;

import com.github.aliaksandrrachko.telegram.bot.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for handling {@link CallbackQuery} requests onto specific handler method.
 *
 * @see CallbackQuery
 * @see UpdateReceiver
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CallBack {

    /**
     * @return supported values {@link CallbackQuery#getData()}
     */
    String[] value() default {};

    String regexp() default "";
}
