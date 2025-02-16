package org.example.dbms.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dbms.constants.Datatype;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Table {
  private volatile AtomicInteger autoIncrementId;
  private Map<Integer, Row> entries;
  private String tableName;
  private Set<Column> schema;

  private Map<Column, HashSet<Object>> uniqueColumnData;

  public Table(String tableName, Set<Column> schema) {
    this.autoIncrementId = new AtomicInteger(0);
    this.entries = new ConcurrentHashMap<>();
    this.tableName = tableName;
    this.schema = new HashSet<>(schema);
    this.uniqueColumnData = new HashMap<>();

    initUniqueIndex();
  }

  private void initUniqueIndex() {
    schema.add(Column
            .builder()
            .unique(true)
            .columnName("id")
            .type(Datatype.INT)
            .build());

    for (Column column: schema) {
      if (column.isUnique()) {
        uniqueColumnData.putIfAbsent(column, new HashSet<>());
      }
    }
  }

  public void addRow(Map<Column, Object> newEntry) {
    dataSanityCheck(newEntry);

    Map<Column, Object> entry = new HashMap<>();
    Column primaryKey = Column.builder()
            .columnName("id")
            .type(Datatype.INT)
            .unique(true)
            .build();
    entry.put(primaryKey, autoIncrementId.get());

    for (Column column: schema) {
      Object data = newEntry.get(column);
      entry.put(column, data);
      if (column.isUnique()) {
        uniqueColumnData.computeIfAbsent(column, k -> new HashSet<>()).add(data);
      }
    }

    entries.put(autoIncrementId.get(), new Row(autoIncrementId.getAndIncrement(), entry));
  }

  public void updateRow(Integer rowId, Map<Column, Object> entry) {
    Row row = entries.get(rowId);

    for (Column column: row.getData().keySet()) {
      if (column.isUnique() && entry.containsKey(column)) {

        uniqueColumnData.get(column).remove(row.getData().get(column));

        if (uniqueColumnData.get(column).contains(entry.get(column))) {
          throw new RuntimeException("Uniqueness constraint is failing");
        }
      }
    }


    row.getData().putAll(entry);
  }


  public void deleteRow(Integer rowId) {
    Row row = entries.get(rowId);

    for (Column column: row.getData().keySet()) {
      if (column.isUnique()) {
        uniqueColumnData.get(column).remove(row.getData().get(column));
      }
    }

    entries.remove(rowId);
  }


  private void dataSanityCheck(Map<Column, Object> newEntry) {

    for (Column column: schema) {
      if ("id".equals(column.getColumnName())) {
        continue;
      }
      if (!newEntry.containsKey(column)) {
        throw new RuntimeException("Column not present in the given data");
      }

      if (column.isUnique() && uniqueColumnData.get(column).contains(newEntry.get(column))) {
        throw new RuntimeException(String.format("Uniqueness constrain is breaking for column %s", column.getColumnName()));
      }
    }

  }

  public List<Map<Column, Object>> filterRows(Map<Column, HashSet<Object>> filterCriteria) {
    List<Map<Column, Object>> res = new ArrayList<>();
    for (Row row: entries.values()) {

      boolean found = true;

      for (Column column: filterCriteria.keySet()) {
        if (!row.getData().containsKey(column) || !filterCriteria.get(column).contains(row.getData().get(column))) {
          found = false;
        }
      }

      if (found) {
        res.add(row.getData());
      }

    }

    return res;
  }


  private boolean isDataEqual(Column column, Object newData, Object value) {
    switch (column.getType()) {
      case BOOL -> {
        return !(newData instanceof Boolean) || newData.equals(value);
      }
      case INT -> {
        return !(newData instanceof Integer) || newData.equals(value);
      }
      case VARCHAR -> {
        return !(newData instanceof String) || newData.equals(value);
      }
      default -> {
        return false;
      }
    }
  }
}
