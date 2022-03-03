package io.github.aliaksandrrachko.telegram.bot.user.service;

public interface IUserStateService {

    void setState(Long id, String state);

    String getState(Long id);
}
