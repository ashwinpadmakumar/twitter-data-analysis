/**
 * Description: Custom Banner for Startup.
 *
 * @author: Ashwin Padmakumar
 * @since: 2021-11-29
 * @version: 0.1
 */

package com.workspace.client;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import com.workspace.config.KafkaConfig;

@Component
public class KafkaConsumerClient<K, V> {

  private final KafkaConfig kafkaConfig;

  public KafkaConsumerClient(KafkaConfig kafkaConfig) {
    this.kafkaConfig = kafkaConfig;
  }

  private Properties getProperties() {
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServer());
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "kafka-demo-elasticsearch");
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");

    return properties;
  }

  public KafkaConsumer<K, V> get() {
    return new KafkaConsumer<>(getProperties());
  }
}
