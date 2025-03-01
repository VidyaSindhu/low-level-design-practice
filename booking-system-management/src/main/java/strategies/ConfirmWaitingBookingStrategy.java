package strategies;

import models.Booking;

import java.time.LocalDateTime;
import java.util.TreeSet;

public interface ConfirmWaitingBookingStrategy {
  Booking confirmWaitingBooking(TreeSet<Booking> bookings);
}
