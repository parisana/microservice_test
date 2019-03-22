package com.swift.consumer.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swift.consumer.model.SwiftData;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

/**
 * @author Parisana
 */
public class SwiftDataDeserializer implements Deserializer<SwiftData> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public SwiftData deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, SwiftData.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void close() {

    }
}
