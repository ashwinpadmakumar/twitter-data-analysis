/**
 * Description: Custom Banner for Startup.
 *
 * @author: Ashwin Padmakumar
 * @since: 2021-11-29
 * @version: 0.1
 */

package com.workspace.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.kafka")
public class KafkaConfig {

  private String bootstrapServer;
  private String topic;

  public String getBootstrapServer() {
    return bootstrapServer;
  }

  public void setBootstrapServer(String bootstrapServer) {
    this.bootstrapServer = bootstrapServer;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
}
