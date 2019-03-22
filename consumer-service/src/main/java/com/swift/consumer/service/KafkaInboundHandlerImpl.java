package com.swift.consumer.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.networknt.server.ShutdownHookProvider;
import com.networknt.service.SingletonServiceFactory;
import com.swift.consumer.database.MongoDatabaseProvider;
import com.swift.consumer.model.SwiftData;
import com.swift.consumer.serialization.SwiftDataDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @author Parisana
 */
public class KafkaInboundHandlerImpl implements KafkaInboundHandler, ShutdownHookProvider {

    private final Object consumerMonitor = new Object();

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final KafkaConsumer<String, SwiftData> consumer;

    private final MongoDatabase mongoDatabase;

    List<TopicPartition> partitions = new ArrayList<>();

    public KafkaInboundHandlerImpl(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "swift_g1");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                SwiftDataDeserializer.class.getName());

        consumer = new KafkaConsumer<>(props);
        log.info("Creating new consumer");

        mongoDatabase = MongoDatabaseProvider.getMongoDatabase();

        initSubscription("test");
    }

    private void initSubscription(String topic) {
        // all partitions for test is assigned to partitionInfos
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        for (PartitionInfo partitionInfo: partitionInfos){
            partitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
        }
        // start subscription on all partition of this topic
        consumer.assign(partitions);
        startConsuming((swiftData)->{
            swiftData.setRead_timestamp(DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            log.info("Event: Read timestamp added. your_name: " + swiftData.getYour_name());

            saveToMongoDB(swiftData);
        });
    }

    private void saveToMongoDB(SwiftData swiftData) {
        final MongoCollection<Document> collection = mongoDatabase.getCollection(SwiftData.class.getSimpleName());
        final Document document;
        try {
            document = new Document(SwiftData.class.getDeclaredField("your_name").getName(), swiftData.getYour_name())
                    .append(SwiftData.class.getDeclaredField("incoming_timestamp").getName(), swiftData.getIncoming_timestamp())
                    .append(SwiftData.class.getDeclaredField("read_timestamp").getName(), swiftData.getRead_timestamp());
            collection.insertOne(document);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private void startConsuming(Consumer<SwiftData> callback){
        System.out.println(mongoDatabase);
        try {
            while (true) {
                ConsumerRecords<String, SwiftData> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, SwiftData> record : records) {
                    callback.accept(record.value());
                }
                consumer.commitAsync((offsets, exception)->{
                    if (exception!=null)
                        log.error(exception.getMessage());
                });
            }
        }catch (KafkaException ex){
            log.error(ex.getMessage());
        } finally {
            try {
                consumer.commitSync();
            } finally {
                closeConsumer();
            }
        }
    }

    private void closeConsumer() {
        log.info("closing consumer!");
        consumer.close();
    }

    @Override
    public void onShutdown() {
        try {
            consumer.wakeup();
            consumer.close();
        }catch (KafkaException ex){
            log.error(ex.getMessage());
        }
    }
}
