package com.swift.rest.handler;

import com.networknt.handler.LightHttpHandler;
import com.networknt.service.SingletonServiceFactory;
import com.swift.rest.model.SwiftData;
import com.swift.rest.service.KafkaProducerHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class PostHandler implements LightHttpHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaProducerHandler kafkaProducerHandler;

    public PostHandler() {
        this.kafkaProducerHandler = SingletonServiceFactory.getBean(KafkaProducerHandler.class);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        logger.info("Request for post data received");
        final SwiftData swiftData = new SwiftData();
        swiftData.setYour_name(Integer.parseInt(exchange.getQueryParameters().get(SwiftData.class.getDeclaredField("your_name").getName()).getFirst()));
        swiftData.setIncoming_timestamp(DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
        kafkaProducerHandler.sendToTopicTest(UUID.randomUUID().toString(), swiftData);
        exchange.getResponseHeaders().add(new HttpString("Content-Type"), "application/json");
        exchange.getResponseSender().send("Success!");
    }
}
