package daos;

import lombok.Data;
import models.LockerTransactionLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class LockerTransactionActivityLogDao {
  private final Map<Integer, List<LockerTransactionLog>> lockerTransactionActivityMap;

  public LockerTransactionActivityLogDao() {
    this.lockerTransactionActivityMap = new ConcurrentHashMap<>();
  }

  public void addLockerTransactionActivity(int lockerId, int orderId) {
    lockerTransactionActivityMap.computeIfAbsent(lockerId, k -> new ArrayList<>())
            .add(getLockerTransactionLog(orderId));
  }

  private static LockerTransactionLog getLockerTransactionLog(int orderId) {
    return LockerTransactionLog.builder()
            .orderId(orderId)
            .servedOn(LocalDateTime.now())
            .build();
  }

  public List<LockerTransactionLog> getLockerActivityLog(int lockerId) {
    return lockerTransactionActivityMap.getOrDefault(lockerId, new ArrayList<>())
            .stream()
            .sorted(Comparator.comparing(LockerTransactionLog::getServedOn))
            .toList();

  }
}
