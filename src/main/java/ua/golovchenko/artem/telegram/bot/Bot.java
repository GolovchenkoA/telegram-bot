package ua.golovchenko.artem.telegram.bot;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.golovchenko.artem.telegram.model.AppCounter;

@Component
public class Bot extends TelegramLongPollingBot {

    public static final Logger LOGGER = LoggerFactory.getLogger("bot");

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private final MeterRegistry registry;

    @Autowired
    public Bot(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onUpdateReceived(Update update) {

        registry.counter(AppCounter.MESSAGE_COUNTS.name(), "isBot",
                String.valueOf(update.getMessage().isUserMessage()) ).increment();

        logMessage(update);

        SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text("Hi!")
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void logMessage(Update update) {
        User from = update.getMessage().getFrom();
        String firstName = from.getFirstName();
        String lastName = from.getLastName();

        String info = String.format("%d %s %s", update.getMessage().getMessageId(), firstName, lastName);
        String msg = String.format("Message have gotten from %s", info);

        LOGGER.info(msg);
    }
}
