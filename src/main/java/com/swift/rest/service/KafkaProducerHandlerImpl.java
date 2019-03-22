package com.swift.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author Parisana
 */
public class KafkaProducerHandlerImpl implements KafkaProducerHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Properties props;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Producer<String, String> producer;

    public KafkaProducerHandlerImpl() {
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("delivery.timeout.ms", 30001);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public <K, V> void sendToTopicTest(K key, Integer partition, V o) throws KafkaException, JsonProcessingException {
        Producer<String, String> producer = getKafkaProducer();
        checkNotNull(producer, "Kafka Producer cannot be null");
//        final ObjectMapper objectMapper = SingletonServiceFactory.getBean(ObjectMapper.class);
        final ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test", partition, objectMapper.writeValueAsString(key), objectMapper.writeValueAsString(o));
        producer.send(producerRecord, (metadata, ex)->{
            try {
                if (ex == null){
                    logger.info("Sent ok: " + producerRecord + ", metadata: " + metadata);
                } else throw new KafkaException(ex.getMessage());
            } finally {
                closeProducer(producer);
            }
        });
    }

    @Override
    public <K, V> void sendToTopicTest(K key, V o) throws KafkaException, JsonProcessingException {
        Producer<String, String> producer = getKafkaProducer();
        checkNotNull(producer, "Kafka Producer cannot be null");
//        final ObjectMapper objectMapper = SingletonServiceFactory.getBean(ObjectMapper.class);
        final ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test", objectMapper.writeValueAsString(key), objectMapper.writeValueAsString(o));
        producer.send(producerRecord, (metadata, ex)->{
            try {
                if (ex == null){
                    logger.info("Sent ok: " + producerRecord + ", metadata: " + metadata);
                } else throw new KafkaException(ex.getMessage());
            } finally {
                closeProducer(producer);
            }
        });
    }

    private <T> void checkNotNull(T obj, String message) {
        if (obj == null)
            throw new RuntimeException(message);
    }

    private <K, V> void closeProducer(Producer<K, V> producer) {
        producer.close();
    }

    private Producer<String, String> getKafkaProducer() {
        if (this.producer == null){
            synchronized (this){
                if (this.producer==null){
                    this.producer = new KafkaProducer<>(props);
                }
            }
        }
        return this.producer;
    }

}
