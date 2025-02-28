

/*
Problem statement:-
Design a models.Locker Management system for an e-commerce store.
While performing a purchase, the users can opt for their packages to get delivered to a models.Locker system close to their place.
The delivery person will place the package in a locker.
An OTP will be generated and sent to the user once the package is added to the locker.

As per the user’s convenience, they can visit the locker, enter the OTP, and get their parcel.
Further, if the users want to return the goods, they can visit the locker and place the item.
The delivery guy will get an OTP which can be used to unlock the locker.

The following are the expectations from the system:-
The person asks the system to allocate a locker for a given package. Assume random allocation for now.
The system should be extensible and accommodate the allocation of locker based on the size of the input package
The system must generate a code/OTP and send it to the user along with the locker details.
The system should be extensible enough so that Tomorrow's user can be verified through email verification and fingerprint.
The user can enter the OTP & locker details to unlock the locker
Once the package is taken out, the locker can be allocated for any other order
Give me all orders that have been served by a particular locker

Evaluation criteria
Code readability
Extensibility & reusability
Modularity
Testability

models.Locker {
  lockerId,
  location,
  lockerStatus,
  lockerSize
  orderId (has a relationship)
  enums.VerificationMode
}


models.Order {
  orderId,
  productId,
  orderStatus,
  createdAt,
  completedAt,
}

Otp extends verificationMode {
  code

  boolean isOTPCorrect(otp) {
  this.otp == code
  }
}

services.LockerService {
  Map<lockerId, models.Locker> lockerMap
}

services.LockerManager {
  Map<lockerId, List<LockerTransaction>> activityLog
  getOrder (orderId);
  returnOrder(orderId);
}

 */


import services.LockerManager;

public class Application {
  private static LockerManager lockerManager;

  public static void main(String[] args) {
    lockerManager = LockerManager.getInstance();
  }
}
