package org.telegram.bot.core.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.bot.core.services.IUserStateService;

@Configuration
@ComponentScan(basePackages = {"org.telegram.bot.core"})
@ConditionalOnProperty(prefix = "bot", name = {"name", "token"})
@ConditionalOnBean(value = {IUserStateService.class})
public class TelegramBotConfig {

}
