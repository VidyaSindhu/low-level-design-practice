package services;

import daos.LockersDao;
import enums.LockerSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import models.Locker;
import models.Order;
import models.VerificationChannel;
import services.lockerassignmentstrategies.LockerAssignmentStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@AllArgsConstructor
public class LockerService {

  private final LockersDao lockersDao;

  public LockerService() {
    this.lockersDao = new LockersDao();
  }

  public Locker assignLocker(LockerAssignmentStrategy lockerAssignmentStrategy, Order order, VerificationChannel verificationChannel) {
    Locker locker = lockerAssignmentStrategy.assignLocker(lockersDao.getAvailableLockers(), order);

    locker.assignOrder(order);
    locker.setVerificationChannel(verificationChannel);

    return lockersDao.makeLockerOccupied(locker.getLockerId());
  }

  public Order getOrder(int lockerId) {
    return Optional.ofNullable(lockersDao.getOccupiedLocker(lockerId))
            .map(Locker::getOrder)
            .orElseThrow(() -> new RuntimeException("models.Order not found in the given lockerId: " + lockerId));
  }

  public Order releaseLocker(int lockerId, VerificationChannel verificationChannel) {
    Locker locker = Optional.ofNullable(lockersDao.getOccupiedLocker(lockerId))
            .orElseThrow(() -> new RuntimeException("models.Locker not found in the given lockerId: " + lockerId));

    isVerificationChannelValid(locker, verificationChannel);
    Order order = locker.getOrder();

    lockersDao.makeLockerAvailable(locker.getLockerId());
    return order;
  }


  public void addLocker(LockerSize lockerSize, String lockerLocation) {
    lockersDao.addNewLocker(lockerSize, lockerLocation);
  }

  public void isVerificationChannelValid(Locker locker, VerificationChannel verificationChannel) {
    if (!verificationChannel.getVerificationMode().equals(locker.getVerificationChannel().getVerificationMode())) {
      throw new RuntimeException("Wrong verification Channel provided");
    }

    if (!locker.getVerificationChannel().isValid(verificationChannel)) {
      throw new RuntimeException("verification channel selected, can not verified, please check again");
    };
  }
}
