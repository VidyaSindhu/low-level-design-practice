package models;

import enums.LockerSize;
import enums.LockerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Locker {
  /*
   lockerId,
  location,
  lockerStatus,
  lockerSize
  orderId (has a relationship)
  enums.VerificationMode
   */

  private Integer lockerId;
  private String location;
  private LockerStatus lockerStatus;
  private LockerSize lockerSize;
  private Order order;
  private VerificationChannel verificationChannel;

  public boolean isAvailable() {
    return LockerStatus.AVAILABLE.equals(lockerStatus);
  }

  public void assignOrder(Order order) {
    this.order = order;
    this.lockerStatus = LockerStatus.OCCUPIED;
  }

  public void releaseLocker() {
    this.order = null;
    this.verificationChannel = null;
    this.lockerStatus = LockerStatus.AVAILABLE;
  }
}
