package models;

import enums.EventGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@AllArgsConstructor
public class Event {
  private final int eventId;
  private final EventGenre eventGenre;
  private final String eventName;

  private final Map<LocalDate, Map<Integer, Show>> showMap;

  private final AtomicInteger showIdGenerator = new AtomicInteger(0);

  public Event(int eventId, String eventName, EventGenre eventGenre) {
    this.eventId = eventId;
    this.eventName = eventName;
    this.eventGenre = eventGenre;
    this.showMap = new ConcurrentHashMap<>();
  }

  public void addShow(LocalTime startTime, int capacity) {
    Show show = Show.builder()
        .showId(showIdGenerator.incrementAndGet())
        .event(this)
        .startTime(startTime)
        .lengthInHours(1)
        .capacity(capacity)
        .build();

    showMap.putIfAbsent(LocalDate.now(), new ConcurrentHashMap<>());


    for (Map.Entry<Integer, Show> showEntry : showMap.get(LocalDate.now()).entrySet()) {
      Show existingShow = showEntry.getValue();
      LocalTime startExisting = existingShow.getStartTime();
      LocalTime endExisting = startExisting.plusHours(1);

      LocalTime startNew = show.getStartTime();
      LocalTime endNew = startNew.plusHours(1);

      if (startExisting.isBefore(endNew) && startNew.isBefore(endExisting)) {
        throw new IllegalArgumentException("Show already exists at this time");
      }
    }

    showMap.get(LocalDate.now()).put(show.getShowId(), show);
  }

  public void deleteShow(int showId) {
    showMap.get(LocalDate.now()).remove(showId);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return Objects.equals(eventName, event.eventName);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(eventName);
  }

  public boolean areSeatsAvailable(LocalDateTime startTime) {
    return showMap.get(LocalDate.now()).values().stream()
        .filter(show -> show.getStartTime().equals(startTime))
        .allMatch(Show::areSeatsAvailable);
  }

  public Show getShow(LocalTime startTime) {
    return showMap.get(LocalDate.now()).values().stream()
        .filter(show -> show.getStartTime().equals(startTime))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Show not found"));
  }

  public void decrementBookedSeats(LocalTime startTime, int numberOfSeats) {
    Show show = getShow(startTime);
    show.setBookedSeats(show.getBookedSeats() - numberOfSeats);
  }
}
