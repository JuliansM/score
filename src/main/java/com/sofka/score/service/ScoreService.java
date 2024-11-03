package com.sofka.score.service;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.score.model.Score;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class ScoreService {

    private final ServiceBusSenderAsyncClient senderClient;
    private final ObjectMapper objectMapper;

    public ScoreService(
            @Value("${spring.cloud.azure.servicebus.connection-string}") String connectionString,
            @Value("${spring.cloud.azure.servicebus.topics.send.name}") String requestTopicName,
            ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;

        this.senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .topicName(requestTopicName)
                .buildAsyncClient();
    }

    public Mono<Void> sendMessage(String message) {
        return senderClient.sendMessage(new ServiceBusMessage(message))
                .doOnError(error -> System.err.println("Error al enviar mensaje: " + error.getMessage()));
    }

    public Mono<Void> processMessage(String message) {

        return Mono.fromCallable(() -> objectMapper.readValue(message, new TypeReference<List<Score>>() {}))
                .flatMap(this::scoreSum)
                .flatMap(integer -> sendMessage(integer.toString()));
    }

    public Mono<Integer> scoreSum(List<Score> scores) {
        return Mono.just(scores.stream()
                .map(Score::getValue)
                .reduce(0, Integer::sum));
    }

}
