package io.github.aliaksandrrachko.telegram.bot.user.model;

import io.github.aliaksandrrachko.telegram.bot.abstraction.AEntity;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends AEntity<Long> {

    private String username;
    private String firstName;
    private String lastName;

    private String state;
    private Long chatId;
    private String languageCode;

    @Default
    private ZonedDateTime created = ZonedDateTime.now().withZoneSameInstant(ZoneId.ofOffset("GMT", ZoneOffset.of("+3")));
    private ZonedDateTime updated;
}
