package org.example.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.models.MessageQueue;
import org.example.models.QueueEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@AllArgsConstructor
public class QueueManagerImpl implements QueueManager {

  private final Map<String, String> publishers;
  private final Map<String, String> subscribers;
  private MessageQueue messageQueue;

  public QueueManagerImpl() {
    this.publishers = new ConcurrentHashMap<>();
    this.subscribers = new ConcurrentHashMap<>();
  }

  public QueueManagerImpl(String queueName) {
    this();
    this.messageQueue = new MessageQueue(queueName);
  }


  @Override
  public void publishMessage(QueueEvent queueEvent, String publisherId) {

    messageQueue.pushMessage(queueEvent);
    //TODO push the event to queue activity log
  }

  @Override
  public void registerPublisher(String publisher) {
    publishers.put(publisher, publisher);
  }

  @Override
  public void deRegisterPublisher(String publisherId) {
    publishers.remove(publisherId);
  }

  @Override
  public QueueEvent getNextEvent() {
    QueueEvent queueEvent = messageQueue.pollMessage();
    //add the queue event to activity log
    return queueEvent;
  }

  @Override
  public void addSubscriber(String subscriber) {
    subscribers.put(subscriber, subscriber);
  }
}
