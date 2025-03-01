package strategies;

import models.Booking;

import java.util.TreeSet;

public class ConfirmWaitingBookingStrategyImpl implements ConfirmWaitingBookingStrategy {
  @Override
  public Booking confirmWaitingBooking(TreeSet<Booking> bookings) {
    for(Booking booking : bookings){
      if (areRequiredSeatsAvailable(booking)){
        return booking;
      }
    }
    return null;
  }

  private boolean areRequiredSeatsAvailable(Booking booking){
    return booking.getShow().areSeatsAvailable(booking.getNumberOfSeats());
  }
}
