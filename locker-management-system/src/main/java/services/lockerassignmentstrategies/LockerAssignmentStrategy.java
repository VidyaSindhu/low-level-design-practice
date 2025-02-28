package services.lockerassignmentstrategies;

import models.Locker;
import models.Order;

import java.util.List;

public interface LockerAssignmentStrategy {
  Locker assignLocker(List<Locker> lockers, Order order);
}
