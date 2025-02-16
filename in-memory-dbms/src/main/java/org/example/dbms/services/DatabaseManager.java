package org.example.dbms.services;

import org.example.dbms.models.Column;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface DatabaseManager {
  void addNewTable(String tableName, Set<Column> tableSchema);
  void dropTable(String tableName);

  void addRow(String tableName, Map<Column, Object> newEntry);
  void updateRow(String tableName, Integer rowId, Map<Column, Object> updatedEntry);
  void removeRow(String tableName, Integer rowId);
  void printAllRows(String tableName);
  void filterRows(String tableName, Map<Column, HashSet<Object>> filterCriteria);
}
