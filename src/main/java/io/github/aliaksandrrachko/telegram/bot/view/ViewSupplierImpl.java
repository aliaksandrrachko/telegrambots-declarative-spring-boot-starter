package io.github.aliaksandrrachko.telegram.bot.view;

import io.github.aliaksandrrachko.telegram.bot.exception.UnsupportedViewException;
import io.github.aliaksandrrachko.telegram.bot.view.ViewSupplier;
import io.github.aliaksandrrachko.telegram.bot.view.View;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import io.github.aliaksandrrachko.telegram.bot.view.GenericListView;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
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
    @SuppressWarnings("unchecked")
    public <T> View<T> get(T methodResult, Method m) {
        Type genericReturnType = m.getGenericReturnType();
        String typeName = genericReturnType.getTypeName();
        String parametrizedType = StringUtils.substringBetween(typeName, "<", ">");

        Class<?> genericsType;
        if (parametrizedType != null) {
            genericsType = Class.forName(parametrizedType);
        } else {
            genericsType = (Class<?>) genericReturnType;
        }

        return (View<T>) getSupportingViewByClass(methodResult, genericsType);
    }

    private View<?> getSupportingViewByClass(Object o, Class<?> aClass) {
        if (o instanceof Collection) {
            return getSupportingIViewByCollectionTypeAndRowType(aClass);
        } else {
            return getSupportingIViewByType(o.getClass());
        }
    }

    // todo: think about different collections Set, List
    private View<?> getSupportingIViewByCollectionTypeAndRowType(Class<?> aClass) {
        View<?> supportingIViewByType = getSupportingIViewByType(aClass);
        return new GenericListView(supportingIViewByType, aClass);
    }

    private View<?> getSupportingIViewByType(Class<?> aClass) {
        return views.parallelStream().filter(iView -> iView.supports(aClass)).findAny()
                .orElseThrow(() -> new UnsupportedViewException(aClass));
    }
}
