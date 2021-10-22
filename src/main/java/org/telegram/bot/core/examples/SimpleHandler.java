package org.telegram.bot.core.examples;

import org.telegram.bot.core.annotations.Command;
import org.telegram.bot.core.annotations.Handler;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Handler()
public class SimpleHandler {

    @Command(value = "/simple")
    public String simpleMessageHandler(Message message){
        return "Simple handler";
    }

    @Command(value = "/simpleList")
    public List<String> simpleMessageListHandler(Message message) {
        return Stream.of("1" , "2", "3").collect(Collectors.toList());
    }
}
