package io.github.aliaksandrrachko.telegram.bot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Lazy
@Slf4j
public class UserStateServiceDefaultImpl implements IUserStateService {

    private Map<Long, String> states;

    @PostConstruct
    private void initCache() {
        log.warn("Create " + IUserStateService.class.getName() +
                "with database implementation, will be using cache implementation "
                + UserStateServiceDefaultImpl.class.getName());
        states = new ConcurrentHashMap<>();
    }

    @Override
    public void setState(Long id, String state) {
        states.put(id, state);
    }

    @Override
    public String getState(Long id) {
        return states.getOrDefault(id, null);
    }
}
