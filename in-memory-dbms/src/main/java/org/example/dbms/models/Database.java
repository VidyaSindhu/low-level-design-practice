package org.example.dbms.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@AllArgsConstructor
public class Database {
  private String databaseName;
  private Map<String, Table> tables;

  public Database(String databaseName) {
    this.databaseName = databaseName;
    this.tables = new ConcurrentHashMap<>();
  }

  public void createNewTable(String tableName, Set<Column> schema) {
    if (tables.containsKey(tableName)) {
      throw new RuntimeException(String.format("Table with tableName: %s is already present", tableName));
    }

    tables.put(tableName, new Table(tableName, schema));
  }

  public void dropTable(String tableName) {
    tables.remove(tableName);
  }
}
