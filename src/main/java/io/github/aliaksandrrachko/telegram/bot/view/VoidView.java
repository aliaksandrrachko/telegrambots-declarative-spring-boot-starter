package io.github.aliaksandrrachko.telegram.bot.view;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
public class VoidView implements View<Void> {

    @Override
    public Class<Void> getGenericType() {
        return Void.class;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String id) {
        return Collections.emptyList();
    }

    @Override
    public boolean supports(Class<?> entityClass) {
        return Void.class.isAssignableFrom(entityClass);
    }
}
