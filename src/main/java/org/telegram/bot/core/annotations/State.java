package org.telegram.bot.core.annotations;

import org.springframework.stereotype.Component;
import org.telegram.bot.core.UpdateReceiver;
import org.telegram.bot.core.services.IUserStateService;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for handling {@link Message} requests onto specific handler method,
 * for user with stored state {@link IUserStateService#getState(Long)}.
 *
 * @see IUserStateService
 * @see Message
 * @see UpdateReceiver
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface State {

    /**
     * @return supported states {@link IUserStateService#getState(Long)}
     */
    String[] value();
}
