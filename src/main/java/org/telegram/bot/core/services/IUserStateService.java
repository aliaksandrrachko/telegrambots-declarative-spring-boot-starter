package org.telegram.bot.core.services;

public interface IUserStateService {

    void setState(Long id, String state);

    String getState(Long id);
}
