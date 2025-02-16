package org.example.ratingsystem.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ratingsystem.constants.EntityType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
  private EntityType type;
  private int entityId;
  //other things
}
