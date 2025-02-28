package services.lockerassignmentstrategies.impl;

import models.Locker;
import models.Order;
import services.lockerassignmentstrategies.LockerAssignmentStrategy;

import java.util.List;

public class LockerAssignmentStrategyImpl implements LockerAssignmentStrategy {
  @Override
  public Locker assignLocker(List<Locker> lockers, Order order) {
    if (lockers.isEmpty()) {
      throw new RuntimeException("no lockers available for assignment");
    }
    return lockers.get(0);
  }
}
