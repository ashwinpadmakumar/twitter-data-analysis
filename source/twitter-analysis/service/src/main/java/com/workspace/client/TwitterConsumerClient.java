/**
 * Description: Custom Banner for Startup.
 *
 * @author: Ashwin Padmakumar
 * @since: 2021-11-29
 * @version: 0.1
 */

package com.workspace.client;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.stereotype.Component;

import com.workspace.config.TwitterConsumerConfig;

@Component
public class TwitterConsumerClient {

  private final BlockingQueue<String> messageQueue;
  private final TwitterConsumerConfig twitterConsumerConfig;

  public TwitterConsumerClient(TwitterConsumerConfig twitterConsumerConfig) {
    this.messageQueue = new LinkedBlockingQueue<>(100000);
    this.twitterConsumerConfig = twitterConsumerConfig;
  }

  public Client get() {
    Hosts hoseBirdHosts = new HttpHosts(Constants.STREAM_HOST);
    StatusesFilterEndpoint hoseBirdEndpoint = new StatusesFilterEndpoint();
    hoseBirdEndpoint.trackTerms(new ArrayList<>(twitterConsumerConfig.getTerms()));
    Authentication hoseBirdAuth = new OAuth1(
        twitterConsumerConfig.getConsumerKey(),
        twitterConsumerConfig.getConsumerSecret(),
        twitterConsumerConfig.getToken(),
        twitterConsumerConfig.getTokenSecret()
    );

    ClientBuilder builder = new ClientBuilder()
        .name("HoseBird-Client-01")
        .hosts(hoseBirdHosts)
        .authentication(hoseBirdAuth)
        .endpoint(hoseBirdEndpoint)
        .processor(new StringDelimitedProcessor(messageQueue));

    Client hoseBirdClient = builder.build();
    return hoseBirdClient;
  }

  public BlockingQueue<String> getMessageQueue() {
    return messageQueue;
  }
}
