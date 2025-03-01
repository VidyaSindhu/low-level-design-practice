package models;

import enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class Booking implements Comparable<Booking> {
  private final int bookingId;
  private final String userId;
  private final Show show;
  private BookingStatus bookingStatus;
  private final int numberOfSeats;
  private LocalDateTime bookedAt;


  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Booking booking = (Booking) o;
    return userId == booking.userId && Objects.equals(show, booking.show);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, show);
  }

  @Override
  public int compareTo(Booking o) {
    return bookedAt.compareTo(o.bookedAt);
  }
}
