package org.telegram.bot.core.entities;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum State {
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    LANGUAGE("/language"),
    LISTEN("/listen"),
    SUBSCRIBE("/subscribe"),
    GET_EVENT("/get_event"),
    WRITE_EVENT_DATE("/write_event_date"),
    NONE("");

    private static final Map<String, State> mapState;

    static {
        mapState = Stream.of(State.values()).collect(Collectors.toMap(State::getStateName, value -> value));
    }

    @Getter
    private final String stateName;

    State(String command) {
        this.stateName = command;
    }

    public static State getSate(String command) {
        return mapState.getOrDefault(command, State.NONE);
    }
}
