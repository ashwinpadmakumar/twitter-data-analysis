/**
 * Description: Custom Banner for Startup.
 *
 * @author: Ashwin Padmakumar
 * @since: 2021-11-29
 * @version: 0.1
 */

package com.workspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.workspace.service.Consumer;
import com.workspace.service.Producer;

@SpringBootApplication
public class Application {

  public static void main(String[] args) throws InterruptedException {
    ApplicationContext context = SpringApplication.run(Application.class, args);
    Producer producer = context.getBean("producer", Producer.class);
    Consumer consumer = context.getBean("consumer", Consumer.class);

    producer.run();
    consumer.run();
  }
}
