/**
 * Description: Custom Banner for Startup.
 *
 * @author: Ashwin Padmakumar
 * @since: 2021-11-29
 * @version: 0.1
 */

package com.workspace.client;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

import com.workspace.config.ElasticsearchProducerConfig;

@Component
public class ElasticsearchProducerClient {

  private final ElasticsearchProducerConfig elasticsearchProducerConfig;

  public ElasticsearchProducerClient(ElasticsearchProducerConfig elasticsearchProducerConfig) {
    this.elasticsearchProducerConfig = elasticsearchProducerConfig;
  }

  public RestHighLevelClient get() {
    String hostname = elasticsearchProducerConfig.getHostname();
    int port = elasticsearchProducerConfig.getPort();
    String scheme = elasticsearchProducerConfig.getScheme();
    RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port, scheme));
    return new RestHighLevelClient(builder);
  }
}
