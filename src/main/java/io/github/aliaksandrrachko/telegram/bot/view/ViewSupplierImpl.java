package io.github.aliaksandrrachko.telegram.bot.view;

import io.github.aliaksandrrachko.telegram.bot.exception.UnsupportedViewException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class ViewSupplierImpl implements ViewSupplier {

    private final List<View<?>> views;

    public ViewSupplierImpl(List<View<?>> views) {
        this.views = views;
    }

    @Override
    @SneakyThrows
    public <T> View<T> get(T methodResult, Type type) {
        if (type instanceof Class<?> clazz) {
            if (clazz.isArray()) {
                Class<?> arrayClass = clazz.componentType();
                throw new UnsupportedViewException(clazz);
            } else {
                return get(clazz);
            }
        } else if (type instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Class<?> genericType = (Class<?>) Arrays.stream(actualTypeArguments).findAny().orElse(null);
            return getParametrizedTypeView(methodResult.getClass(), genericType);
        } else if (type instanceof GenericArrayType) {
            throw new UnsupportedViewException(type.getClass());
        } else if (type instanceof TypeVariable) {
            throw new UnsupportedViewException(type.getClass());
        }
        throw new UnsupportedViewException(type.getClass());
    }

    @SuppressWarnings("unchecked")
    private <T> View<T> getParametrizedTypeView(Class<?> aClass, Class<?> genericsType) {
        if (Collection.class.isAssignableFrom(aClass)) {
            return (View<T>) new CollectionView(this, genericsType);
        }
        throw new UnsupportedViewException(aClass);
    }

    @SuppressWarnings("unchecked")
    public <T> View<T> get(Class<?> aClass) {
        return (View<T>) views.parallelStream().filter(iView -> iView.supports(aClass)).findAny()
                .orElseThrow(() -> new UnsupportedViewException(aClass));
    }
}
