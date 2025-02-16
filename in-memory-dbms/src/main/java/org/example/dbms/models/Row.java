package org.example.dbms.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Row {
  private int rowId;
  private Map<Column, Object> data;

  public Row(int rowId) {
    this.rowId = rowId;
    this.data = new ConcurrentHashMap<>();
  }
}
