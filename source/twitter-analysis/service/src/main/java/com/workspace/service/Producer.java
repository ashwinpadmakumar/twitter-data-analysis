/**
 * Description: Custom Banner for Startup.
 *
 * @author: Ashwin Padmakumar
 * @since: 2021-11-29
 * @version: 0.1
 */

package com.workspace.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.twitter.hbc.core.Client;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.workspace.client.KafkaProducerClient;
import com.workspace.client.TwitterConsumerClient;
import com.workspace.config.KafkaConfig;


@Component
public class Producer {

  private static final Logger logger = LoggerFactory.getLogger(Producer.class);

  private final TwitterConsumerClient twitterConsumerClient;
  private final KafkaProducerClient<String, String> kafkaProducerClient;
  private final KafkaConfig kafkaConfig;


  public Producer(TwitterConsumerClient twitterConsumerClient,
                  KafkaProducerClient<String, String> kafkaProducerClient,
                  KafkaConfig kafkaConfig) {
    this.twitterConsumerClient = twitterConsumerClient;
    this.kafkaProducerClient = kafkaProducerClient;
    this.kafkaConfig = kafkaConfig;
  }

  public void run() {
    logger.info("Starting Producer...");
    Client twitterClient = twitterConsumerClient.get();
    twitterClient.connect();
    BlockingQueue<String> messageQueue = twitterConsumerClient.getMessageQueue();
    while (!twitterClient.isDone()) {
      try {
        String message = messageQueue.poll(20, TimeUnit.SECONDS);
        if (message != null) {
          String key = extractIdFromTweet(message);
          pushToKafka(key, message);
        }
      } catch (InterruptedException exception) {
        logger.error("Exception occurred in Producer. Message {}", exception.getMessage());
        twitterClient.stop();
      }
    }
  }

  private void pushToKafka(String key, String value) {
    KafkaProducer<String, String> producer = kafkaProducerClient.get();
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
        kafkaConfig.getTopic(),
        key,
        value
    );
    Callback callback = (metadata, exception) -> {
      if (exception != null) {
        logger.error("Exception Caught : {}", exception.getMessage());
      }
    };
    producer.send(producerRecord, callback);
  }

  private String extractIdFromTweet(String tweet) {
    JsonObject jsonObject = new Gson().fromJson(tweet, JsonObject.class);
    return jsonObject.get("id_str").getAsString();
  }
}
