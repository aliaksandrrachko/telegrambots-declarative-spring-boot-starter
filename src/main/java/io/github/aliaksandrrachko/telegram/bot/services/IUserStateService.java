package io.github.aliaksandrrachko.telegram.bot.services;

public interface IUserStateService {

    void setState(Long id, String state);

    String getState(Long id);
}
