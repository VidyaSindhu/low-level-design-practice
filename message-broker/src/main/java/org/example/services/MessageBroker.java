package org.example.services;

import org.example.models.QueueEvent;
import org.example.models.QueueEventActivityLog;

import java.util.List;

public interface MessageBroker {
  String addQueue(String queueName);
  void addSubscriber(String queueName, String subscriberId);
  void addPublisher(String queueName, String publisherId);
  void publishEvent(String queueName, QueueEvent queueEvent, String publisher);
  QueueEvent getNextEvent(String queueName, String subscriberId);

  List<QueueEventActivityLog> getQueueMessageHistory(String queueName);
}
