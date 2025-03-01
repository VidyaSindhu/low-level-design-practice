package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class Show implements Comparable<Show> {
  private final int showId;
  private final Event event;
  private final LocalTime startTime;
  private final int lengthInHours;
  private final int capacity;
  private int bookedSeats;

  static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

  public boolean areSeatsAvailable(int requiredSeats) {
    return requiredSeats <= capacity - bookedSeats;
  }

  public boolean areSeatsAvailable() {
    return capacity - bookedSeats > 0;
  }

  public void incrementBookedSeats(int seats) {
    bookedSeats += seats;
  }

  public void decrementSeats(int seats) {
    bookedSeats -= seats;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Show show = (Show) o;
    return Objects.equals(event, show.event) && Objects.equals(startTime, show.startTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event, startTime);
  }

  @Override
  public int compareTo(Show o) {
    return getStartTime().compareTo(o.getStartTime());
  }

  @Override
  public String toString() {
    return String.format("%s: (%s-%s) %s", event.getEventName(), startTime.format(formatter), startTime.plusHours(lengthInHours).format(formatter), capacity-bookedSeats);
  }
}
