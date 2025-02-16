package org.example.dbms.services;

import lombok.val;
import org.example.dbms.models.Column;
import org.example.dbms.models.Database;
import org.example.dbms.models.Row;

import java.util.*;


public class DatabaseManagerImpl implements DatabaseManager {

  private static volatile DatabaseManager DATABASE_MANAGER;

  public static DatabaseManager getInstance(String databaseName) {
    if (DATABASE_MANAGER == null) {
     synchronized (DatabaseManagerImpl.class) {
       if (DATABASE_MANAGER == null) {
         DATABASE_MANAGER = new DatabaseManagerImpl(databaseName);
       }
     }
    }

    return DATABASE_MANAGER;
  }

  private DatabaseManagerImpl(String databaseName) {
    this.database = new Database(databaseName);
  }

  private final Database database;


  @Override
  public void addNewTable(String tableName, Set<Column> tableSchema) {
    database.createNewTable(tableName, tableSchema);
  }

  @Override
  public void dropTable(String tableName) {
    database.dropTable(tableName);
  }

  @Override
  public void addRow(String tableName, Map<Column, Object> newEntry) {
    database.getTables().get(tableName)
            .addRow(newEntry);
  }

  @Override
  public void updateRow(String tableName, Integer rowId, Map<Column, Object> updatedEntry) {
    database.getTables().get(tableName)
            .updateRow(rowId, updatedEntry);
  }

  @Override
  public void removeRow(String tableName, Integer rowId) {
    database.getTables().get(tableName)
            .deleteRow(rowId);
  }

  @Override
  public void printAllRows(String tableName) {

    val rows = database.getTables().get(tableName)
            .getEntries().values()
            .stream()
            .sorted(Comparator.comparing(Row::getRowId))
            .map(Row::getData)
            .toList();

    printRows(rows);
  }

  private static void printRows(List<Map<Column, Object>> rows) {
    if (rows.size() > 0) {
      Set<Column> columns = rows.get(0).keySet();
      for (Column column: columns) {
        System.out.print(column.getColumnName() + "  |  ");
      }
      System.out.print("\n");


      for (val row: rows) {
        for (Column column: columns) {
          System.out.print(row.get(column) + "  |  ");
        }
        System.out.print("\n");
      }

      StringBuilder sb = new StringBuilder();
      sb.append("-".repeat(80));
      sb.append("\n");
      System.out.println(sb.toString());
    }


  }

  @Override
  public void filterRows(String tableName, Map<Column, HashSet<Object>> filterCriteria) {
    val rows = database.getTables().get(tableName)
            .filterRows(filterCriteria);

    printRows(rows);
  }
}
