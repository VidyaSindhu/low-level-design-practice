package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class QueueEvent {
  private String eventId;
  private byte[] message;
}
