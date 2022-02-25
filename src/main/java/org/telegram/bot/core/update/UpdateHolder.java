package org.telegram.bot.core.update;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Holder for {@link Update}
 */
@UtilityClass
public class UpdateHolder {

    private static final ThreadLocal<Update> UPDATE_HOLDER = new ThreadLocal<>();

    public void set(Update value) {
        clear();
        UPDATE_HOLDER.set(value);
    }

    public Update get() {
        return UPDATE_HOLDER.get();
    }

    public void clear() {
        UPDATE_HOLDER.remove();
    }
}
