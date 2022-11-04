/**
 * Description: Custom Banner for Startup.
 *
 * @author: Ashwin Padmakumar
 * @since: 2021-11-29
 * @version: 0.1
 */

package com.workspace.service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.workspace.client.ElasticsearchProducerClient;
import com.workspace.client.KafkaConsumerClient;
import com.workspace.config.ElasticsearchProducerConfig;
import com.workspace.config.KafkaConfig;


@Component
public class Consumer {

  private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

  private final KafkaConsumerClient<String, String> kafkaConsumerClient;
  private final ElasticsearchProducerClient elasticsearchProducerClient;
  private final KafkaConfig kafkaConfig;
  private final ElasticsearchProducerConfig elasticsearchProducerConfig;

  public Consumer(KafkaConsumerClient<String, String> kafkaConsumerClient,
                  ElasticsearchProducerClient elasticsearchProducerClient,
                  KafkaConfig kafkaConfig,
                  ElasticsearchProducerConfig elasticsearchProducerConfig) {
    this.kafkaConsumerClient = kafkaConsumerClient;
    this.elasticsearchProducerClient = elasticsearchProducerClient;
    this.kafkaConfig = kafkaConfig;
    this.elasticsearchProducerConfig = elasticsearchProducerConfig;
  }

  public void run() {
    KafkaConsumer<String, String> consumer = kafkaConsumerClient.get();
    consumer.subscribe(List.of(kafkaConfig.getTopic()));

    while (true) {
      /* Poll for every 10 seconds */
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));

      int recordCount = records.count();
      logger.info("Received {} records", recordCount);

      for (ConsumerRecord<String, String> consumerRecord : records) {
        pushToES(consumerRecord.key(), consumerRecord.value());
      }
    }
  }

  private void pushToES(String id, String doc) {
    RestHighLevelClient esClient = elasticsearchProducerClient.get();
    try {
      var indexRequest = new IndexRequest(elasticsearchProducerConfig.getIndex())
          .id(id)
          .source(doc, XContentType.JSON);
      var indexResponse = esClient.index(indexRequest, RequestOptions.DEFAULT);
      logger.info("Inserted record in ES with id [{}]", indexResponse.getId());
      Thread.sleep(1000);
    } catch (NullPointerException | IOException | InterruptedException exception) {
      logger.error("Exception occurred in Consumer. Message {}", exception.getMessage());
      logger.warn("Skipping bad data: \n Key: {} \n Value: {}", id, doc);
    }
  }
}
