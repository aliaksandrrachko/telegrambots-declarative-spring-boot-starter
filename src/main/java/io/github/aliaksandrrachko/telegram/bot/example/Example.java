package io.github.aliaksandrrachko.telegram.bot.example;

import io.github.aliaksandrrachko.telegram.bot.annotation.Handler;
import io.github.aliaksandrrachko.telegram.bot.annotation.Command;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
        return message.getFrom().getFirstName();
    }

    @Command(value = "/integer")
    public Integer getId(Message message) {
        return (int) (Math.random() * 100);
    }

    @Command(value = "/declarative_bot_example_count")
    public List<String> count(Message message) {
        return Stream.of("1", "2", "3").collect(Collectors.toList());
    }

    @Command(value = "/examples")
    public List<String> examples(Message message, Update update, CallbackQuery callbackQuery) {
        List<String> result = new ArrayList<>();
        result.add("Command:");
        result.add("declarative_bot_example_get_my_name");
        result.add("integer");
        result.add("declarative_bot_example_count");
        return result;
    }

    @Command(value = "/array")
    public String[] arrayExample(Message message){
        return new String[] {"array", "line1", "line2"};
    }

    @Command(value = "/listOfList")
    public List<List<String>> listOfList(Message message){
        List<List<String>> listOfList = new ArrayList<>();
        listOfList.add(new ArrayList<>());
        listOfList.add(new ArrayList<>());
        return listOfList;
    }
}
