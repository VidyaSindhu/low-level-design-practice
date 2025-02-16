package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
@Builder
@AllArgsConstructor
public class MessageQueue {
  private String queueName;
  private final Queue<QueueEvent> queue;

  public MessageQueue() {
    this.queue = new ConcurrentLinkedQueue<>();
  }

  public MessageQueue(String queueName) {
    this();
    this.queueName = queueName;
  }

  public void pushMessage(QueueEvent queueEvent) {
    queue.offer(queueEvent);
  }

  public QueueEvent pollMessage() {
    return queue.poll();
  }

}
