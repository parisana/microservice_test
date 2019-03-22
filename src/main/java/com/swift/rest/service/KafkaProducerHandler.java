package com.swift.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.common.KafkaException;

/**
 * @author Parisana
 */
public interface KafkaProducerHandler {
    <K, V> void sendToTopicTest(K key, Integer partition, V o) throws KafkaException, JsonProcessingException;

    <K, V> void sendToTopicTest(K key, V o) throws KafkaException, JsonProcessingException;
}
