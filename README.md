# Declarative Telegram Bot Spring Boot Starter

[![Build Status]()]()
[![Maven Central]()]()
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](https://github.com/aliaksandrrachko/telegrambots-declarative-spring-boot-starter/blob/master/LICENSE)

A simple-to-use spring boot starter to create Telegram Bots in Java

----------------

## Contributions
Feel free to fork this project, work on it and then make a pull request against **DEV** branch. Most of the times I will accept them if they add something valuable to the code.

Please, **DO NOT PUSH ANY TOKEN OR API KEY**, I will never accept a pull request with that content.

----------------

## Usage

Just add starter dependency to your project with one of these options:

1. Using Maven Central Repository:

```xml
    <dependency>
        <groupId>org.telegram</groupId>
        <artifactId>telegrambots-declarative-spring-boot-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

2. Using Gradle:

```gradle
    implementation 'org.telegram:telegrambots-declarative-spring-boot-starter:0.0.1-SNAPSHOT'
```

3. Download the jar(including all dependencies) from [here](https://mvnrepository.com/artifact/org.telegram/telegrambots/5.3.0)

----------------

## Example bots

----------------

## How to use

1. Your main spring boot class should look like this:

```java
@SpringBootApplication
public class YourApplicationMainClass {

	public static void main(String[] args) {		
		SpringApplication.run(YourApplicationMainClass.class, args);
	}
}
```

2. Create telegram bot with [BotFather](https://telegram.me/botfather)
3. Adds properties to 

`application.yaml`
```yml
bot:
  name: bot_name
  token: bot_token
```

or 

`application.properties`

```properties
bot.name=bot_name
bot.token=bot_token
```

4. After that your bot commands handlers will look like:

```java
import CallBack;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

// Handlers annotation
@Handler
public class StartHandler {

    // Command annotation or Callback
    @Command(value = "/start")
    @CallBack(value = "/start")
    public String startHandlerMethod(Message message) {
        // some logic here
        return "Hi!"; // your response
    }

    @State(value = "/write_date")
    public SendMessage getEvents(Message message) {
        String messageText = message.getText(); // get writing data
        SendMessage sendMessage = new SendMessage();
        // some logic here else
        sendMessage.setText("Thanks you!");
        return sendMessage; // response
    }
}
```

See another annotation in package [org.telegram.bot.core.annotation](/com/github/aliaksandrrachko/telegram/bot/core/annotations)

## Telegram Bot API
This library use [Telegram bot API](https://core.telegram.org/bots),
[TelegramBots](https://github.com/rubenlagus/TelegramBots), 
you can find more information following the link.

----------------

## Questions or Suggestions
Feel free to create issues [here](https://github.com/aliaksandrrachko/telegrambots-declarative-spring-boot-starter/issues)

----------------