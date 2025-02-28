package daos;

import enums.LockerSize;
import enums.LockerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import models.Locker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@AllArgsConstructor
public class LockersDao {
  private final AtomicInteger autoIncrementLockerId;
  private final Map<Integer, Locker> availableLockersMap;
  private final Map<Integer, Locker> occupiedLockersMap;

  public LockersDao() {
    this.availableLockersMap = new ConcurrentHashMap<>();
    this.autoIncrementLockerId = new AtomicInteger(0);
    this.occupiedLockersMap = new ConcurrentHashMap<>();
  }

  public void addNewLocker(LockerSize lockerSize, String lockerLocation) {
    Locker locker = Locker.builder()
            .lockerSize(lockerSize)
            .location(lockerLocation)
            .lockerStatus(LockerStatus.AVAILABLE)
            .lockerId(autoIncrementLockerId.getAndIncrement())
            .build();

    availableLockersMap.put(locker.getLockerId(), locker);
  }

  public List<Locker> getAvailableLockers() {
    return availableLockersMap.values().stream().toList();
  }

  public Locker makeLockerOccupied(int lockerId) {
    Locker locker = availableLockersMap.get(lockerId);
    availableLockersMap.remove(lockerId);
    occupiedLockersMap.put(locker.getLockerId(), locker);
    return locker;
  }

  public void makeLockerAvailable(int lockerId) {
    Locker locker = occupiedLockersMap.get(lockerId);
    occupiedLockersMap.remove(lockerId);
    locker.releaseLocker();
    availableLockersMap.put(lockerId, locker);
  }

  public Locker getOccupiedLocker(int lockerId) {
    return occupiedLockersMap.get(lockerId);
  }
}
