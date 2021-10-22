package org.telegram.bot.core.services;

import org.springframework.context.annotation.Lazy;

import javax.ws.rs.ext.ParamConverter;

@Lazy
public interface IUserStateService {

    void setState(Long id, String state);

    String getState(Long id);
}
