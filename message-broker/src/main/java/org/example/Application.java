package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.QueueEvent;
import org.example.services.MessageBroker;
import org.example.services.MessageBrokerImpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Application {

  public static void main(String[] args) {
    MessageBroker messageBroker = MessageBrokerImpl.getInstance();

    String queueName = "temp_queue";
    messageBroker.addQueue("temp_queue");
    List<Thread> threadList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Thread thread = new Thread(new AddEvents(messageBroker));
      threadList.add(thread);
      thread.start();
    }

    threadList.forEach(
            thread -> {
              try {
                thread.join();
              } catch (InterruptedException e) {
                //
              }
            }
    );

    System.out.println(messageBroker.getQueueMessageHistory(queueName));
    System.out.println(messageBroker.getQueueMessageHistory(queueName).size());

    System.out.println(messageBroker.getNextEvent(queueName, UUID.randomUUID().toString()));

    System.out.println(messageBroker.getQueueMessageHistory(queueName));
  }
}

class AddEvents implements Runnable {

  private final MessageBroker messageBroker;
  private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public AddEvents(MessageBroker messageBroker) {
    this.messageBroker = messageBroker;
  }

  @Override
  public void run() {
    messageBroker.publishEvent("temp_queue", QueueEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .message(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8))
            .build(), UUID.randomUUID().toString());
  }
}
