import enums.EventGenre;
import models.Event;
import models.Show;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EventManager {
  private final Map<EventGenre, Map<String, Set<Show>>> eventShowMap; // <eventType, <eventName, Show>>

  private final Map<String, Event> eventMap; // <eventName, Event>
  private final AtomicInteger eventIdGenerator = new AtomicInteger(0);

  //make this class singleton
  private static volatile EventManager eventManager;

  public static EventManager getInstance() {
    if (eventManager == null) {
      synchronized (EventManager.class) {
        if (eventManager == null) {
          eventManager = new EventManager();
        }
      }
    }
    return eventManager;
  }

  private EventManager() {
    eventShowMap = new ConcurrentHashMap<>();
    eventMap = new ConcurrentHashMap<>();
  }

  public void registerEvent(String eventName, EventGenre eventGenre) {
    eventShowMap.putIfAbsent(eventGenre, new ConcurrentHashMap<>());
    eventShowMap.get(eventGenre).putIfAbsent(eventName, ConcurrentHashMap.newKeySet());
    eventMap.putIfAbsent(eventName, Event.builder()
        .eventId(eventIdGenerator.incrementAndGet())
        .eventName(eventName)
        .eventGenre(eventGenre)
        .showMap(new ConcurrentHashMap<>())
        .build());
  }

  public void addShow(String eventName, LocalTime startTime, LocalTime endTime, int capacity) {

    if (startTime.plusHours(1).isAfter(endTime)) {
      throw new IllegalArgumentException("Show should be 1 hour long");
    }

    Event event = eventMap.get(eventName);
    if (event == null) {
      throw new IllegalArgumentException("Event does not exist with name: " + eventName);
    }
    eventShowMap.get(event.getEventGenre()).get(eventName).add(Show.builder()
        .showId(event.getShowIdGenerator().incrementAndGet())
        .event(event)
        .startTime(startTime)
        .lengthInHours(1)
        .capacity(capacity)
        .build());
    event.addShow(startTime, capacity);
  }

  public void printShows(EventGenre eventGenre) {
    if (!eventShowMap.containsKey(eventGenre)) {
      throw new IllegalArgumentException("No shows found for genre: " + eventGenre);
    }

    for (Map.Entry<String, Set<Show>> eventEntry : eventShowMap.get(eventGenre).entrySet()) {
      System.out.println(eventEntry.getKey());
      for (Show show : eventEntry.getValue()) {
        System.out.println(show);
      }
    }
  }

  private boolean areSeatsAvailable(String eventName, LocalDateTime startTime) {
    Event event = eventMap.get(eventName);
    if (event == null) {
      throw new IllegalArgumentException("Event does not exist with name: " + eventName);
    }

    return event.areSeatsAvailable(startTime);
  }

  public Show getShow(String eventName, LocalTime startTime) {
    Event event = eventMap.get(eventName);
    if (event == null) {
      throw new IllegalArgumentException("Event does not exist with name: " + eventName);
    }

    return event.getShow(startTime);
  }

  public void decrementBookedSeats(String eventName, LocalTime startTime, int numberOfSeats) {
    Event event = eventMap.get(eventName);
    if (event == null) {
      throw new IllegalArgumentException("Event does not exist with name: " + eventName);
    }

    event.decrementBookedSeats(startTime, numberOfSeats);
  }


}
