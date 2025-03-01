import enums.BookingStatus;
import enums.EventGenre;
import lombok.Getter;
import lombok.val;
import models.Booking;
import strategies.ConfirmWaitingBookingStrategy;
import strategies.ConfirmWaitingBookingStrategyImpl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class BookingSystemManager {
  private static volatile BookingSystemManager bookingSystemManager;

  private final Map<Integer, Map<BookingStatus, Set<Booking>>> showBookingMap; // <showId, <BookingStatus, Set<Booking>>>

  private final Map<Integer, Booking> bookingMap; // <bookingId, Booking>

  private final Map<String, Set<Integer>> userBookingMap; // <userId, Set<bookingId>>

  private final AtomicInteger bookingIdGenerator = new AtomicInteger(0);
  private final EventManager eventManager;

  private final ConfirmWaitingBookingStrategy confirmWaitingBookingStrategy;


  public static BookingSystemManager getInstance() {
    if (bookingSystemManager == null) {
      synchronized (BookingSystemManager.class) {
        if (bookingSystemManager == null) {
          bookingSystemManager = new BookingSystemManager();
        }
      }
    }
    return bookingSystemManager;
  }

  private BookingSystemManager() {
    bookingMap = new ConcurrentHashMap<>();
    showBookingMap = new ConcurrentHashMap<>();
    eventManager = EventManager.getInstance();
    userBookingMap = new ConcurrentHashMap<>();
    confirmWaitingBookingStrategy = new ConfirmWaitingBookingStrategyImpl();
  }

  public void registerEvent(String eventName, EventGenre eventGenre) {
    eventManager.registerEvent(eventName, eventGenre);
  }

  public void addShow(String eventName, LocalTime startTime, LocalTime endTime, int capacity) {
    eventManager.addShow(eventName, startTime, endTime, capacity);
  }


  public void printShows(EventGenre eventGenre) {
    eventManager.printShows(eventGenre);
  }

  // input is like this (UserA, TMKOC, 12:00, 2)
  public int bookShowTicket(String userId, String eventName, LocalTime startTime, int numberOfSeats) {

    Booking booking = Booking.builder()
            .bookingId(bookingIdGenerator.incrementAndGet())
            .userId(userId)
            .show(eventManager.getShow(eventName, startTime))
            .bookingStatus(BookingStatus.WAITING)
            .numberOfSeats(numberOfSeats)
            .bookedAt(LocalDateTime.now())
            .build();

    //show has available seats
    if (booking.getShow().areSeatsAvailable(numberOfSeats)) {
//      eventManager.decrementBookedSeats(eventName, startTime, numberOfSeats);
      booking.getShow().incrementBookedSeats(numberOfSeats);
      booking.setBookingStatus(BookingStatus.CONFIRMED);
    }

    bookingMap.put(booking.getBookingId(), booking);
    showBookingMap.computeIfAbsent(booking.getShow().getShowId(), k -> new ConcurrentHashMap<>())
            .computeIfAbsent(booking.getBookingStatus(), k -> new TreeSet<>()).add(booking);
    userBookingMap.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(booking.getBookingId());

    return booking.getBookingId();
  }

  public void cancelBooking(int bookingId) {

    Booking booking = bookingMap.get(bookingId);

    if (booking == null) {
      throw new IllegalArgumentException("Booking not found with id: " + bookingId);
    }

    if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
      throw new IllegalArgumentException("Booking already cancelled with id: " + bookingId);
    }

    boolean wasConfirmed = booking.getBookingStatus() == BookingStatus.CONFIRMED;
    removeBookingFromShowMap(booking);

    booking.setBookingStatus(BookingStatus.CANCELLED);

    addBookingInShowMap(booking);

    if (!wasConfirmed) {
      return;
    }

    booking.getShow().decrementSeats(booking.getNumberOfSeats());

    Set<Booking> waitingBookings = showBookingMap.get(booking.getShow().getShowId()).get(BookingStatus.WAITING);
    Booking nextBooking = confirmWaitingBookingStrategy.confirmWaitingBooking(new TreeSet<>(waitingBookings));
    if (nextBooking != null) {
      nextBooking.getShow().incrementBookedSeats(nextBooking.getNumberOfSeats());

      removeBookingFromShowMap(nextBooking);

      nextBooking.setBookingStatus(BookingStatus.CONFIRMED);
      addBookingInShowMap(nextBooking);
    }
  }

  private void addBookingInShowMap(Booking booking) {
    showBookingMap.get(booking.getShow().getShowId())
            .computeIfAbsent(booking.getBookingStatus(), k -> new TreeSet<>())
            .add(booking);
  }

  private void removeBookingFromShowMap(Booking booking) {
    showBookingMap.get(booking.getShow().getShowId()).get(booking.getBookingStatus()).remove(booking);

    if (showBookingMap.get(booking.getShow().getShowId()).get(booking.getBookingStatus()).isEmpty()) {
      showBookingMap.get(booking.getShow().getShowId()).remove(booking.getBookingStatus());
    }
  }

  public void printAllBookingsForAShow(String eventType, LocalTime startTime) {
    System.out.println("----------------------------------");
    int showId = eventManager.getShow(eventType, startTime).getShowId();
    val showBookings = showBookingMap.get(showId);
    for (Map.Entry<BookingStatus, Set<Booking>> entry : showBookings.entrySet()) {
      System.out.println(entry.getKey());
      for (Booking booking : entry.getValue()) {
        System.out.println(booking);
      }
    }

    System.out.println("----------------------------------");
  }

  public void printAllUserBookings(String userId) {
    val userBookings = userBookingMap.get(userId);
    System.out.println("User: " + userId);
    for (int bookingId : userBookings) {
      System.out.println(bookingMap.get(bookingId));
    }
    System.out.println("----------------------------------");
  }
}
