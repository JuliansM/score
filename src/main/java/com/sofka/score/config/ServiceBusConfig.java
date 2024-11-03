package com.sofka.score.config;

import com.sofka.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
public class ServiceBusConfig {

    private final ScoreService scoreService;

    @Bean
    public Consumer<Message<String>> consume() {
        return message -> {
            System.out.printf("New message received: '%s'.%n", message.getPayload());
            scoreService.processMessage(message.getPayload())
                    .subscribe();
        };
    }
}
