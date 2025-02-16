package org.example.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.models.QueueEvent;
import org.example.models.QueueEventActivityLog;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@AllArgsConstructor
public class QueueActivityLogManager {
  private String queueActivityLogName;
  private volatile Map<String, QueueEventActivityLog> queueEventActivityLogMap;

  public QueueActivityLogManager(String queueActivityLogName) {
    this.queueActivityLogName = queueActivityLogName;
    this.queueEventActivityLogMap = new ConcurrentHashMap<>();
  }

  public void addQueueEvent(String publisherId, QueueEvent queueEvent) {
    queueEventActivityLogMap.putIfAbsent(queueEvent.getEventId(),
            new QueueEventActivityLog(queueEvent, publisherId, LocalDateTime.now()));
  }


  public void updateConsumedEvent(String subscriberId, QueueEvent queueEvent) {
    QueueEventActivityLog  queueEventActivityLog = queueEventActivityLogMap.get(queueEvent.getEventId());
    queueEventActivityLog.setConsumedAt(LocalDateTime.now());
    queueEventActivityLog.setSubscriberId(subscriberId);
  }

  public List<QueueEventActivityLog> getActivityLog() {
    return queueEventActivityLogMap.values().stream()
            .sorted(Comparator.comparing(QueueEventActivityLog::getPublishedAt))
            .toList();
  }
}
