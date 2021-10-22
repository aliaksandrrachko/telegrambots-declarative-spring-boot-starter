package org.telegram.bot.core;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.bot.core.annotations.CallBack;
import org.telegram.bot.core.annotations.Command;
import org.telegram.bot.core.annotations.ExceptionHandler;
import org.telegram.bot.core.annotations.Handler;
import org.telegram.bot.core.annotations.MessageTextQuery;
import org.telegram.bot.core.annotations.State;
import org.telegram.bot.core.exceptions.UnsupportedTelegramBotMappingException;
import org.telegram.bot.core.interfaces.IAnnotationMethodHandlerSupplier;

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
public class AnnotationMethodHandlerSupplierImpl implements IAnnotationMethodHandlerSupplier {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, Object> annotatedHandlers;

    @PostConstruct
    private void setAnnotatedHandlerMap(){
        this.annotatedHandlers = this.applicationContext.getBeansWithAnnotation(Handler.class);
    }

    @Override
    public Map<String, Object> getAllHandlersBean() {
        return this.annotatedHandlers;
    }

    @Override
    public Method getSupportedBotCommandMethod(String... value) {
        return getSupportedValuesMethod(Command.class, value);
    }

    @Override
    public Method getSupportedCallBackMethod(String... value) {
        return getSupportedValuesMethod(CallBack.class, value);
    }

    @Override
    public Method getSupportedMessageTextHandler(String... value) {
        return getSupportedValuesMethod(MessageTextQuery.class, value);
    }

    @Override
    public Method getSupportedStateHandler(String... value) {
        return getSupportedValuesMethod(State.class, value);
    }

    @Override
    public Method getSupportedExceptionHandler(String... value) {
        return getSupportedValuesMethod(ExceptionHandler.class, value);
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

                            if (annotationsMethod.getName().equals("value")){
                                Object valueFromAnnotation = annotationsMethod.invoke(annotation);
                                if (valueFromAnnotation instanceof String) {
                                    valueFieldMatchResult = Arrays.asList(value).contains(valueFromAnnotation);
                                } else if (valueFromAnnotation instanceof String[]){
                                    valueFieldMatchResult = Arrays.stream(value).anyMatch(s -> Arrays.asList((String[]) valueFromAnnotation).contains(s));
                                }
                            }

                            if (annotationsMethod.getName().equals("regexp")){
                                String regexFromAnnotation = (String) annotationsMethod.invoke(annotation);

                                if (!regexFromAnnotation.equals("")){
                                    Pattern regexPattern = Pattern.compile(regexFromAnnotation);
                                    regexFieldMathResult = Arrays.stream(value).anyMatch(s -> regexPattern.matcher(s).matches());
                                }
                            }
                        }

                        // there you can add some 'else'

                        return valueFieldMatchResult || regexFieldMathResult;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                    // there get class of value from annotation
                    return true;
                }).collect(Collectors.toList());

        if (foundMethods.size() != 1){
            throw new UnsupportedTelegramBotMappingException(value);
        } else {
            return foundMethods.get(0);
        }
    }
}
