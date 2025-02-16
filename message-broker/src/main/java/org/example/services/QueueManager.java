package org.example.services;

import org.example.models.QueueEvent;

public interface QueueManager {
  void publishMessage(QueueEvent queueEvent, String publisherId);
  void registerPublisher(String publisher);
  void deRegisterPublisher(String publisherId);
  QueueEvent getNextEvent();
  void addSubscriber(String subscriber);
}
