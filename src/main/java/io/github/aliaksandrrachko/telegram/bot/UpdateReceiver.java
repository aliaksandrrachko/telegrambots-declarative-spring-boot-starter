package io.github.aliaksandrrachko.telegram.bot;

import io.github.aliaksandrrachko.telegram.bot.method.AnnotationMethodHandlerSupplier;
import io.github.aliaksandrrachko.telegram.bot.method.MethodExecutor;
import io.github.aliaksandrrachko.telegram.bot.view.ViewSupplier;
import io.github.aliaksandrrachko.telegram.bot.utils.SendMessageUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.github.aliaksandrrachko.telegram.bot.exception.UnsupportedTelegramBotMappingException;
import io.github.aliaksandrrachko.telegram.bot.view.View;
import io.github.aliaksandrrachko.telegram.bot.user.service.IUserStateService;
import io.github.aliaksandrrachko.telegram.bot.update.UpdateHolder;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static io.github.aliaksandrrachko.telegram.bot.Operation.CALL_BACK;
import static io.github.aliaksandrrachko.telegram.bot.Operation.COMMAND;
import static io.github.aliaksandrrachko.telegram.bot.Operation.MESSAGE_TEXT_HANDLER;
import static io.github.aliaksandrrachko.telegram.bot.Operation.STATE;

@Component
@Slf4j
public class UpdateReceiver {

    private final AnnotationMethodHandlerSupplier methodHandlerSupplier;
    private final ViewSupplier viewSupplier;
    private final IUserStateService userStateService;
    private final MethodExecutor methodExecutor;

    public UpdateReceiver(AnnotationMethodHandlerSupplier methodHandlerSupplier,
                          ViewSupplier viewSupplier,
                          IUserStateService userStateService,
                          MethodExecutor methodExecutor) {
        this.methodHandlerSupplier = methodHandlerSupplier;
        this.viewSupplier = viewSupplier;
        this.userStateService = userStateService;
        this.methodExecutor = methodExecutor;
    }

    @SneakyThrows
    private List<PartialBotApiMethod<? extends Serializable>> process(Method method, Long chatId, Object... args) {
        Object invokesResult = methodExecutor.execute(method, args);
        View<?> supportingViewByClass = viewSupplier.get(invokesResult, method.getGenericReturnType());
        return supportingViewByClass.render(invokesResult, String.valueOf(chatId));
    }

    // todo: needs fix bugs with get events by date 'oops' 'oops'
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        UpdateHolder.set(update);
        // try-catch, so that with a non-existent command, just return an empty list
        final Message message = update.hasCallbackQuery() ? update.getCallbackQuery().getMessage() : update.getMessage();
        try {
            Method supportedBotMethod = null;
            if (isMessageWithCommandWithoutCallBackQuery(update)) {
                supportedBotMethod = methodHandlerSupplier.getSupportedMethod(COMMAND, message.getText());
            } else if (isMessageWithTextWithoutCallBackQuery(update)) {
                String stateForUserById = this.userStateService.getState(update.getMessage().getFrom().getId());
                // operate only message doesn't start with '/'
                if (stateForUserById == null || stateForUserById.isBlank()) {
                    supportedBotMethod = methodHandlerSupplier.getSupportedMethod(MESSAGE_TEXT_HANDLER, message.getText());
                } else {
                    supportedBotMethod = methodHandlerSupplier.getSupportedMethod(STATE, stateForUserById);
                }
            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                // there you can get user id
                supportedBotMethod = methodHandlerSupplier.getSupportedMethod(CALL_BACK, callbackQuery.getData());
            }

            return process(supportedBotMethod, message.getChatId());
        } catch (UnsupportedTelegramBotMappingException | UnsupportedOperationException e) {
            Method supportedExceptionHandlerMethod = this.methodHandlerSupplier.getSupportedExceptionHandler(e.getClass());
            try {
                return process(supportedExceptionHandlerMethod, message.getChatId(), e);
            } catch (Exception ex) {
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

    private boolean isMessageWithTextWithoutCallBackQuery(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }

    private boolean isMessageWithCommandWithoutCallBackQuery(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().getText().startsWith("/");
    }
}
