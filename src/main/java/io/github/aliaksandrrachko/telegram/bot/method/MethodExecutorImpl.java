package io.github.aliaksandrrachko.telegram.bot.method;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import io.github.aliaksandrrachko.telegram.bot.update.UpdateHolder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MethodExecutorImpl implements MethodExecutor {

    private final AnnotationMethodHandlerSupplier annotationMethodHandlerSupplier;

    public MethodExecutorImpl(AnnotationMethodHandlerSupplier annotationMethodHandlerSupplier) {
        this.annotationMethodHandlerSupplier = annotationMethodHandlerSupplier;
    }

    @Override
    @SneakyThrows
    public Object execute(Method method, Object... additionalArgs) {
        Object methodOwner = annotationMethodHandlerSupplier.getOriginalObject(method);
        Object[] args = getMethodArgs(method, additionalArgs);
        return method.invoke(methodOwner, args);
    }

    private Object[] getMethodArgs(Method method, Object... additionalArgs) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        List<Object> objects = new ArrayList<>(Arrays.stream(additionalArgs).toList());
        objects.add(UpdateHolder.get());
        objects.add(UpdateHolder.get() != null ? UpdateHolder.get().getMessage() : null);
        objects.add( UpdateHolder.get() != null ? UpdateHolder.get().getCallbackQuery() : null);

        for (int i = 0; i < parameters.length; i++) {
            setParameterFromSource(parameterTypes[i], i, parameters, objects);
        }
        return parameters;
    }

    private void setParameterFromSource(Class<?> aClass, int index, Object[] destination, List<Object> source) {
        source.stream()
                .filter(aClass::isInstance)
                .findAny()
                .ifPresent(o -> destination[index] = o);
    }
}
