package io.github.aliaksandrrachko.telegram.bot.view;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionView implements View<Collection<?>> {

    private final ViewSupplier viewSupplier;
    protected Class<?> clazz;

    public CollectionView(ViewSupplier viewSupplier, Class<?> clazz) {
        this.viewSupplier = viewSupplier;
        this.clazz = clazz;
    }

    @Override
    public Class<Collection<?>> getGenericType() {
        return null;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String id) {
        Collection<?> collection = (Collection<?>) entity;
        List<PartialBotApiMethod<? extends Serializable>>  sendMessages = new ArrayList<>();
        View<?> view = viewSupplier.get(clazz);
        collection.forEach(e -> {
            List<PartialBotApiMethod<? extends Serializable>> render = view.render(e, id);
            sendMessages.addAll(render);
        });
        return sendMessages;
    }

    @Override
    public boolean supports(Class<?> entityClass) {
        return entityClass != null && Collection.class.isAssignableFrom(entityClass);
    }
}
