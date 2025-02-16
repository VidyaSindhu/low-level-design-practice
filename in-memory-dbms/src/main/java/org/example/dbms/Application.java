package org.example.dbms;

import org.example.dbms.constants.Datatype;
import org.example.dbms.models.Column;
import org.example.dbms.services.DatabaseManager;
import org.example.dbms.services.DatabaseManagerImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Application {
  public static void main(String[] args) {
    try {
      DatabaseManager databaseManager = DatabaseManagerImpl.getInstance("tmp_db");


      Column nameColumn = Column.builder()
              .columnName("user_name")
              .type(Datatype.VARCHAR)
              .unique(false)
              .build();

      Column ageColumn = Column.builder()
              .columnName("age")
              .type(Datatype.VARCHAR)
              .unique(false)
              .build();

      Column mobileNumber = Column.builder()
              .columnName("mobile_number")
              .type(Datatype.VARCHAR)
              .unique(true)
              .build();


      String userTableName = "user";
      databaseManager.addNewTable(userTableName,
              Set.of(nameColumn, ageColumn, mobileNumber));

      databaseManager.addRow(userTableName, Map.ofEntries(
              Map.entry(nameColumn, "Vidya Sindhu Dubey"),
              Map.entry(ageColumn, 25),
              Map.entry(mobileNumber, "1234567892")
      ));

      databaseManager.addRow(userTableName, Map.ofEntries(
              Map.entry(nameColumn, "VSD"),
              Map.entry(ageColumn, 25),
              Map.entry(mobileNumber, "1234567891")
      ));

      databaseManager.printAllRows(userTableName);

//      databaseManager.updateRow(userTableName, 1,
//              Map.ofEntries(
//                      Map.entry(mobileNumber, "1234567890")
//              ));

//      databaseManager.removeRow(userTableName, 1);


      databaseManager.filterRows(userTableName, Map.ofEntries(
              Map.entry(mobileNumber, new HashSet<>(Arrays.asList("1234567891")))
      ));

//      databaseManager.printAllRows(userTableName);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
