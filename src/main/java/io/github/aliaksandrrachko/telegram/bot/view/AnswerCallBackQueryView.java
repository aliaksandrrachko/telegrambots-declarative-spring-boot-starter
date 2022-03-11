package io.github.aliaksandrrachko.telegram.bot.view;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnswerCallBackQueryView implements View<AnswerCallbackQuery> {

    @Override
    public Class<AnswerCallbackQuery> getGenericType() {
        return AnswerCallbackQuery.class;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> render(Object entity, String chatId) {
        List<PartialBotApiMethod<? extends Serializable>> answerCallBackQueries = new ArrayList<>();
        AnswerCallbackQuery answerCallbackQuery = getGenericType().cast(entity);
        answerCallBackQueries.add(answerCallbackQuery);
        return answerCallBackQueries;
    }

    @Override
    public boolean supports(Class<?> entityClass) {
        return AnswerCallbackQuery.class.isAssignableFrom(entityClass);
    }
}
