package com.github.aliaksandrrachko.telegram.bot.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.github.aliaksandrrachko.telegram.bot.services.IUserStateService;

@Configuration
@ComponentScan(basePackages = {"io.github.aliaksandrrachko.telegram.bot"})
@ConditionalOnProperty(prefix = "bot", name = {"name", "token"})
@ConditionalOnBean(value = {IUserStateService.class})
public class TelegramBotConfig {

}
