package io.github.aliaksandrrachko.telegram.bot.example;

import io.github.aliaksandrrachko.telegram.bot.annotation.Handler;
import io.github.aliaksandrrachko.telegram.bot.annotation.Command;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Handlers example class.
 *
 * @deprecated Only for demo class.
 */
@Deprecated(since = "0.0.1-SNAPSHOT")
@Handler
public class Example {

    @Command(value = "/declarative_bot_example_get_my_name")
    public String getName(Message message) {
        return message.getChat().getUserName();
    }

    @Command(value = "/declarative_bot_example_count")
    public List<String> count(Message message) {
        return Stream.of("1", "2", "3").toList();
    }

    @Command(value = "/examples")
    public List<String> examples(Message message, Update update, CallbackQuery callbackQuery) {
        List<String> result = new ArrayList<>();
        result.add("Command:");
        result.add("/declarative_bot_example_get_my_name");
        result.add("/declarative_bot_example_count");
        return result;
    }
}
