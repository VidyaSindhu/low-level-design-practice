package org.example.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.models.QueueEvent;
import org.example.models.QueueEventActivityLog;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@AllArgsConstructor
public class MessageBrokerImpl implements MessageBroker {

  private static MessageBroker MESSAGE_BROKER;

  public static MessageBroker getInstance() {
    if (MESSAGE_BROKER == null) {
      synchronized (MessageBrokerImpl.class) {
        if (MESSAGE_BROKER == null) {
          MESSAGE_BROKER = new MessageBrokerImpl();
        }
      }
    }
    return MESSAGE_BROKER;
  }

  private final Map<String, QueueManager> queueManagerMap;
  private final Map<String, QueueActivityLogManager> queueActivityLogManagerMap;

  private final static String MANAGER_NAME_SUFFIX = "_manager";
  private final static String ACTIVITY_LOG_MANAGER_NAME_SUFFIX = "_activity_log_manager";

  private MessageBrokerImpl() {
    this.queueManagerMap = new ConcurrentHashMap<>();
    this.queueActivityLogManagerMap = new ConcurrentHashMap<>();
  }

  @Override
  public String addQueue(String queueName) {
    String queueManagerName = getQueueManagerName(queueName);
    if (queueManagerMap.containsKey(queueManagerName)) {
      //exception can be thrown here
      return queueManagerName;
    }

    queueManagerMap.putIfAbsent(queueManagerName, new QueueManagerImpl(queueName));
    queueActivityLogManagerMap.putIfAbsent(getQueueActivityLogManagerName(queueName),
            new QueueActivityLogManager(getQueueActivityLogManagerName(queueName)));
    return queueManagerName;
  }

  private static String getQueueManagerName(String queueName) {
    return queueName + MANAGER_NAME_SUFFIX;
  }

  @Override
  public void addSubscriber(String queueName, String subscriberId) {
    String queueManagerName = getQueueManagerName(queueName);
    Optional.ofNullable(queueManagerMap.get(queueManagerName))
            .ifPresent(queueManager -> {
              queueManager.addSubscriber(subscriberId);
            });
  }

  @Override
  public void addPublisher(String queueName, String publisherId) {
    String queueManagerName = getQueueManagerName(queueName);
    Optional.ofNullable(queueManagerMap.get(queueManagerName))
            .ifPresent(queueManager -> {
              queueManager.registerPublisher(publisherId);
            });
  }

  @Override
  public void publishEvent(String queueName, QueueEvent queueEvent, String publisher) {
    String queueManagerName = getQueueManagerName(queueName);
    Optional.ofNullable(queueManagerMap.get(queueManagerName))
            .ifPresent(queueManager ->  {
              queueManager.publishMessage(queueEvent, publisher);
            });

    queueActivityLogManagerMap.get(getQueueActivityLogManagerName(queueName))
            .addQueueEvent(publisher, queueEvent);
  }

  @Override
  public QueueEvent getNextEvent(String queueName, String subscriberId) {
    String queueManagerName = getQueueManagerName(queueName);
    QueueEvent queueEvent = Optional.ofNullable(queueManagerMap.get(queueManagerName))
            .map(QueueManager::getNextEvent)
            .orElseThrow(() -> new RuntimeException("queue is not present or no new message"));

    queueActivityLogManagerMap.get(getQueueActivityLogManagerName(queueName))
            .updateConsumedEvent(subscriberId, queueEvent);
    return queueEvent;
  }

  @Override
  public List<QueueEventActivityLog> getQueueMessageHistory(String queueName) {
    return queueActivityLogManagerMap.get(getQueueActivityLogManagerName(queueName))
            .getActivityLog();
  }

  private String getQueueActivityLogManagerName(String queueName) {
    return queueName + ACTIVITY_LOG_MANAGER_NAME_SUFFIX;
  }
}
