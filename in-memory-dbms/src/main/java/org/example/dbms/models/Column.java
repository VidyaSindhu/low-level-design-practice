package org.example.dbms.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dbms.constants.Datatype;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Column {
  private Datatype type;
  private boolean unique;
  private String columnName;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Column column = (Column) o;
    return Objects.equals(columnName, column.columnName);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(columnName);
  }
}
