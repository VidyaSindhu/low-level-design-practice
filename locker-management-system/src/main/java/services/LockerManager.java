package services;

/*
The person asks the system to allocate a locker for a given package. Assume random allocation for now.
The system should be extensible and accommodate the allocation of locker based on the size of the input package
The system must generate a code/OTP and send it to the user along with the locker details.
The system should be extensible enough so that Tomorrow's user can be verified through email verification and fingerprint.
The user can enter the OTP & locker details to unlock the locker
Once the package is taken out, the locker can be allocated for any other order
Give me all orders that have been served by a particular locker
 */


import daos.LockerTransactionActivityLogDao;
import enums.VerificationMode;
import models.*;
import services.lockerassignmentstrategies.LockerAssignmentStrategy;
import services.lockerassignmentstrategies.impl.LockerAssignmentStrategyImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockerManager {
  private static volatile LockerManager lockerManager;

  private final LockerService lockerService;
  private final LockerAssignmentStrategy lockerAssignmentStrategy;
  private final LockerTransactionActivityLogDao lockerTransactionActivityLogDao;

  public static LockerManager getInstance() {
    if (lockerManager == null) {
      synchronized (LockerManager.class) {
        if (lockerManager == null) {
          lockerManager = new LockerManager();
        }
      }
    }

    return lockerManager;
  }


  private LockerManager() {
    this.lockerService = new LockerService();
    this.lockerAssignmentStrategy = new LockerAssignmentStrategyImpl();
    this.lockerTransactionActivityLogDao = new LockerTransactionActivityLogDao();
  }


  public int assignLocker(Order order, VerificationMode verificationMode) {
    Locker locker =
            lockerService.assignLocker(lockerAssignmentStrategy, order, getVerificationChannel(verificationMode));

    lockerTransactionActivityLogDao.addLockerTransactionActivity(locker.getLockerId(), order.getOrderId());
    return locker.getLockerId();
  }

  public Order unlockAndGetOrder(int lockerId, VerificationChannel verificationChannel) {
    try {
      return lockerService.releaseLocker(lockerId, verificationChannel);
    } catch (RuntimeException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void getAllLockerTransactionLog(int lockerId) {
    System.out.println(lockerTransactionActivityLogDao.getLockerActivityLog(lockerId));
  }

  private VerificationChannel getVerificationChannel(VerificationMode verificationMode) {
    switch (verificationMode) {
      case OTP -> {
        return new OtpVerificationChannel();
      }
      default -> {
        return new OtpVerificationChannel();
      }
    }
  }
}
