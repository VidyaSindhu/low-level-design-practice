package org.example.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueEventActivityLog {
  private QueueEvent queueEvent;
  private String publisherId;
  private String subscriberId;
  private LocalDateTime publishedAt;
  private LocalDateTime consumedAt;

  public QueueEventActivityLog(QueueEvent queueEvent, String publisherId, LocalDateTime publishedAt) {
    this.queueEvent = queueEvent;
    this.publisherId = publisherId;
    this.publishedAt = publishedAt;
  }
}
