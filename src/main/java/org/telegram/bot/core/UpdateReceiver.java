package org.telegram.bot.core;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.bot.core.exceptions.MessageNotFoundAmongMethodArgsException;
import org.telegram.bot.core.exceptions.UnsupportedViewException;
import org.telegram.bot.core.interfaces.IAnnotationMethodHandlerSupplier;
import org.telegram.bot.core.interfaces.IView;
import org.telegram.bot.core.services.IUserStateService;
import org.telegram.bot.core.utils.SendMessageUtil;
import org.telegram.bot.core.views.GenericListView;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class UpdateReceiver {

    @Autowired
    private final IAnnotationMethodHandlerSupplier methodHandlerSupplier;

    @SneakyThrows
    private List<PartialBotApiMethod<? extends Serializable>> executeMethod(Object methodOwner,
                                                                            Method method,
                                                                            Object... args)
            throws InvocationTargetException, IllegalAccessException {
        Object invokesResult = method.invoke(methodOwner, args);

        Type genericReturnType = method.getGenericReturnType();
        String typeName = genericReturnType.getTypeName();
        String parametrizedType = StringUtils.substringBetween(typeName, "<", ">");

        Class<?> genericsType;
        if (parametrizedType != null) {
            genericsType = Class.forName(parametrizedType);
        } else {
            genericsType = (Class<?>) genericReturnType;
        }

        Message message = findMessageObjectFromObjArgs(args);

        IView<?> supportingViewByClass = getSupportingViewByClass(invokesResult, genericsType);
        return supportingViewByClass.render(invokesResult, String.valueOf(message.getChatId()));
    }

    private Message findMessageObjectFromObjArgs(Object... args) {
        return (Message) Arrays.stream(args).parallel()
                .filter(Message.class::isInstance).findAny()
                .orElseThrow(() -> new MessageNotFoundAmongMethodArgsException(args));
    }

    private final IUserStateService userStateService;

    public UpdateReceiver(IAnnotationMethodHandlerSupplier methodHandlerSupplier, IUserStateService userStateService) {
        this.methodHandlerSupplier = methodHandlerSupplier;
        this.userStateService = userStateService;
    }

    @Autowired
    @Setter
    private List<IView<?>> views;

    // todo: needs fix bugs with get events by date 'oops' 'oops'
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        // try-catch, so that with a non-existent command, just return an empty list
        final Message message = update.hasCallbackQuery() ? update.getCallbackQuery().getMessage() : update.getMessage();
        try {
            if (isMessageWithCommandWithoutCallBackQuery(update)) {
                Method supportedBotCommandMethod = methodHandlerSupplier.getSupportedBotCommandMethod(message.getText());
                return executeMethod(
                        methodHandlerSupplier.getOriginalObject(supportedBotCommandMethod),
                        supportedBotCommandMethod,
                        message);
            } else if (isMessageWithTextWithoutCallBackQuery(update)) {
                String stateForUserById = this.userStateService.getState(update.getMessage().getFrom().getId());

                // operate only message doesn't start with '/'
                Method supportedBotCommandMethod;
                if (stateForUserById == null || stateForUserById.isBlank()) {
                    supportedBotCommandMethod = methodHandlerSupplier.getSupportedMessageTextHandler(message.getText());
                } else {
                    supportedBotCommandMethod = methodHandlerSupplier.getSupportedStateHandler(stateForUserById);
                }
                return executeMethod(
                        methodHandlerSupplier.getOriginalObject(supportedBotCommandMethod),
                        supportedBotCommandMethod,
                        message);
            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                // there you can get user id
                Method supportedBotCommandMethod = methodHandlerSupplier.getSupportedCallBackMethod(callbackQuery.getData());
                return executeMethod(
                        methodHandlerSupplier.getOriginalObject(supportedBotCommandMethod),
                        supportedBotCommandMethod,
                        message);
            }
            return getOopsExceptionResponse(message.getChatId(), "Ops. Something went wrong");
        } catch (UnsupportedOperationException | InvocationTargetException | IllegalAccessException e) {
            Method method = this.methodHandlerSupplier.getSupportedExceptionHandler("");
            try {
                return executeMethod(this.methodHandlerSupplier.getOriginalObject(method), method, message, e);
            } catch (InvocationTargetException | IllegalAccessException ex) {
                log.error("Unhandled exception.", ex);
                return getOopsExceptionResponse(message.getChatId(), "Ops. Happened issue. Try later");
            }
        }
    }

    private static List<PartialBotApiMethod<? extends Serializable>> getOopsExceptionResponse(Long chatId, String message){
        List<PartialBotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        SendMessage messageTemplate = SendMessageUtil.createMessageTemplate(chatId);
        messageTemplate.setText(message);
        messages.add(messageTemplate);
        return messages;
    }

    private IView<?> getSupportingViewByClass(Object o, Class<?> aClass) {
        if (o instanceof Collection) {
            return getSupportingIViewByCollectionTypeAndRowType(aClass);
        } else {
            return getSupportingIViewByType(o.getClass());
        }
    }

    private IView<?> getSupportingIViewByCollectionTypeAndRowType(Class<?> aClass) {
        IView<?> supportingIViewByType = getSupportingIViewByType(aClass);
        return new GenericListView(supportingIViewByType, aClass);
    }

    private IView<?> getSupportingIViewByType(Class<?> aClass) {
        return views.parallelStream().filter(iView -> iView.supports(aClass)).findAny()
                .orElseThrow(() -> new UnsupportedViewException(aClass));
    }

    private boolean isMessageWithTextWithoutCallBackQuery(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }

    private boolean isMessageWithCommandWithoutCallBackQuery(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().getText().startsWith("/");
    }
}
