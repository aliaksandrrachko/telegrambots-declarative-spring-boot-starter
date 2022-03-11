package io.github.aliaksandrrachko.telegram.bot.annotation;

import io.github.aliaksandrrachko.telegram.bot.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for handling {@link Message} requests onto specific handler method.
 * <br> In method with this annotation can be injected {@link Message}. </br>
 *
 * @see Message
 * @see UpdateReceiver
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Command {

    /**
     * @return supported values from {@link Message#getText()}
     */
    String[] value();
}
