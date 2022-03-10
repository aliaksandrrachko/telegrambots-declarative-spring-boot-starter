package io.github.aliaksandrrachko.telegram.bot.method;

import io.github.aliaksandrrachko.telegram.bot.Operation;
import io.github.aliaksandrrachko.telegram.bot.annotation.Handler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import io.github.aliaksandrrachko.telegram.bot.annotation.ExceptionHandler;
import io.github.aliaksandrrachko.telegram.bot.exception.UnsupportedTelegramBotMappingException;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
// todo: think about context of handlers (ex. Map<Annotation, ...)
// todo: in the future, adds processes equals 'value' to difference type
public class AnnotationMethodHandlerSupplierImpl implements AnnotationMethodHandlerSupplier {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, Object> annotatedHandlers;

    @PostConstruct
    private void setAnnotatedHandlerMap(){
        this.annotatedHandlers = this.applicationContext.getBeansWithAnnotation(Handler.class);
    }

    @Override
    public Method getSupportedMethod(Operation operation, String... value) {
        return getSupportedValuesMethod(operation.getAnnotationClass(), value);
    }

    @Override
    public Method getSupportedExceptionHandler(Class<? extends Throwable>... values) {
        Class<? extends Annotation> annotationType = ExceptionHandler.class;
        List<Method> annotatedMethods = getAnnotatedMethods(annotationType);
        List<Method> foundMethods = annotatedMethods.stream()
                .filter(method -> {
                    Annotation annotation = method.getAnnotation(annotationType);
                    try {
                        boolean valueFieldMatchClass = false;
                        for (Method annotationsMethod : annotation.annotationType().getDeclaredMethods()) {
                            valueFieldMatchClass = isValueFieldMatchClass(annotation, annotationsMethod, values);
                            // there you can add some 'else'
                        }
                        return valueFieldMatchClass;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                    // there get class of value from annotation
                    return true;
                }).collect(Collectors.toList());

        if (foundMethods.size() != 1){
            return null;
        } else {
            return foundMethods.get(0);
        }
    }

    private boolean isValueFieldMatchClass(Annotation annotation, Method annotationsMethod, Class<? extends Throwable>[] values)
            throws InvocationTargetException, IllegalAccessException {
        if (annotationsMethod.getName().equals("value")){
            Object valueFromAnnotation = annotationsMethod.invoke(annotation);
            if (valueFromAnnotation instanceof Class<?>) {
                return Arrays.asList(values).contains(valueFromAnnotation);
            } else if (valueFromAnnotation instanceof Class<?>[]){
                return Arrays.stream(values).anyMatch(s -> Arrays.asList((Class<?>[]) valueFromAnnotation).contains(s));
            }
        }
        return false;
    }

    @Override
    public Object getOriginalObject(Method method) {
        return annotatedHandlers.values().stream().filter(o -> {
            Class<?> aClass = o.getClass();
            try {
                Method suspectsMethod = aClass.getMethod(method.getName(), method.getParameterTypes());
                return suspectsMethod.equals(method);
            } catch (NoSuchMethodException e) {
                // do nothing
            }
            return false;
        }).findAny().orElse(null);
    }

    private List<Method> getAnnotatedMethods(Class<? extends Annotation> annotationType){
        return new ArrayList<>(this.annotatedHandlers.values()).stream()
                .collect(ArrayList::new, (methods, o) -> {
                    List<Method> collect = Arrays.stream(o.getClass().getMethods())
                            .filter(objectMethod -> objectMethod.isAnnotationPresent(annotationType))
                            .collect(Collectors.toList());
                    methods.addAll(collect);
                }, ArrayList::addAll);
    }

    @SneakyThrows
    private Method getSupportedValuesMethod(Class<? extends  Annotation> annotationType, String... value){
        List<Method> annotatedMethods = getAnnotatedMethods(annotationType);
        List<Method> foundMethods = annotatedMethods.stream()
                .filter(method -> {
                    Annotation annotation = method.getAnnotation(annotationType);
                    try {
                        boolean valueFieldMatchResult = false;
                        boolean regexFieldMathResult = false;
                        for (Method annotationsMethod : annotation.annotationType().getDeclaredMethods()) {
                            valueFieldMatchResult = isValueFieldMatchResult(annotation, annotationsMethod, value);
                            regexFieldMathResult = isRegexFieldMathResult(annotation, annotationsMethod, value);
                            // there you can add some 'else'
                            if (valueFieldMatchResult || regexFieldMathResult){
                                return true;
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                    // there get class of value from annotation
                    return false;
                }).collect(Collectors.toList());

        if (foundMethods.size() != 1){
            throw new UnsupportedTelegramBotMappingException(value);
        } else {
            return foundMethods.stream().findAny().orElseThrow();
        }
    }

    private boolean isRegexFieldMathResult(Annotation annotation, Method annotationsMethod, String[] value)
            throws IllegalAccessException, InvocationTargetException {
        if (annotationsMethod.getName().equals("regexp")){
            String regexFromAnnotation = (String) annotationsMethod.invoke(annotation);

            if (!regexFromAnnotation.equals("")){
                Pattern regexPattern = Pattern.compile(regexFromAnnotation);
                return Arrays.stream(value).anyMatch(s -> regexPattern.matcher(s).matches());
            }
        }
        return false;
    }

    private boolean isValueFieldMatchResult(Annotation annotation, Method annotationsMethod, String[] values)
            throws IllegalAccessException, InvocationTargetException {
        if (annotationsMethod.getName().equals("value")){
            Object valueFromAnnotation = annotationsMethod.invoke(annotation);
            if (valueFromAnnotation instanceof String) {
                return Arrays.asList(values).contains(valueFromAnnotation);
            } else if (valueFromAnnotation instanceof String[]){
                return Arrays.stream(values).anyMatch(s -> Arrays.asList((String[]) valueFromAnnotation).contains(s));
            }
        }
        return false;
    }
}
