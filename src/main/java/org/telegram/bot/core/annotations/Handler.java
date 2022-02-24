package org.telegram.bot.core.annotations;

import org.springframework.stereotype.Component;
import org.telegram.bot.core.UpdateReceiver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Types that carry this annotation are treated as handlers where {@link Command}, {@link CallBack}
 * methods assume response semantics by default.
 *
 * @see UpdateReceiver
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Handler {

    String name() default  "";
}
