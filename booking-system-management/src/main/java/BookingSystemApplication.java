import enums.EventGenre;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
You are required to build an application that lets users book for Live Shows.
The day is divided into time slots of 1 hour each, starting from 9 am to 9 pm.
Organizers of Live Shows can login to the portal and declare their show timings with the capacity for the given day.
Users can login and book tickets for a particular live show / cancel existing bookings.

For simplicity you can assume that
The Live Shows availability is declared for that particular day only.

Functionalities required:
An organizer should be able to register new live shows, and mention genres among (Comedy, Theatre, Tech, Singing.)
An organizer should be able to declare show’s timings in each slot for the day. For example, the slots will be of 1 hour like 9am-10am, 10am-11am.
Users should be able to login(Optional), and search available shows timings based on genre.
The show's timings should be displayed in a ranked fashion. Default ranking strategy should be to rank by start time.
But we should be able to plugin more strategies like show’s review etc in future.
Users should be able to book 1 ticket for the available slot of show. A user can book multiple show tickets in a day.
A user cannot book two show tickets with two different shows in the same time slot.
1 Ticket can contain multiple persons entry.
It can not be partially booked (if available capacity is 2, one booking ticket requests for 3 persons entry then it can not be booked partially)
Users can also cancel a booking, in which case that slot becomes available for someone else to book.
Build a waitlist feature:
If the user wishes to book a slot for a particular live show that is already booked, then add this user to the waitlist.
If the user with whom the ticket is booked originally, cancels the booking, then the first in the waitlist gets the booking.
A user/organizer should be able to view his/her bookings for the day.
Overlapping slots cannot be provided for a particular show. But there could be running different parallel shows irrespective of the genre
Implementing login feature is optional
User registration is not mandatory
Name of Live Show and UserName are their identifiers

 */


public class BookingSystemApplication {
  private static BookingSystemManager bookingSystemManager;
  private static EventManager eventManager = EventManager.getInstance();
  static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
  public static void main(String[] args) {
    bookingSystemManager = BookingSystemManager.getInstance();

    try {
      eventManager.registerEvent("TMKOC", EventGenre.COMEDY);
      eventManager.registerEvent("The Sonu Nigam Live Event", EventGenre.SINGING);

      eventManager.addShow("TMKOC", LocalTime.of(9, 0), LocalTime.of(10, 0), 3);

      eventManager.addShow("The Sonu Nigam Live Event", LocalTime.of(10,0), LocalTime.of(11, 0), 3);
      eventManager.addShow("The Sonu Nigam Live Event",  LocalTime.of(13, 0), LocalTime.of(14, 0), 2);
      eventManager.addShow("The Sonu Nigam Live Event",  LocalTime.of(17, 0), LocalTime.of(18, 0), 1);

      eventManager.printShows(EventGenre.COMEDY);
      eventManager.printShows(EventGenre.SINGING);

      int bookingId = bookingSystemManager.bookShowTicket("UserA", "TMKOC", LocalTime.of(9, 0), 2);
      int bookingId2 = bookingSystemManager.bookShowTicket("UserA", "TMKOC", LocalTime.of(9, 0), 1);
      int bookingId3 = bookingSystemManager.bookShowTicket("UserB", "TMKOC", LocalTime.of(9, 0), 2);
      int bookingId4 = bookingSystemManager.bookShowTicket("UserC", "TMKOC", LocalTime.of(9, 0), 2);

      bookingSystemManager.cancelBooking(bookingId);
      bookingSystemManager.cancelBooking(bookingId3);

      bookingSystemManager.printAllBookingsForAShow("TMKOC", LocalTime.of(9, 0));

      bookingSystemManager.printAllUserBookings("UserA");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static boolean processCommand(String input) {
    if (input.startsWith("registerShow:")) {
      String[] parts = input.substring(13).split(" -> ");
      String name = parts[0].trim();
      String genre = parts[1].trim();
      eventManager.registerEvent(name, EventGenre.getEventGenre(genre));
      System.out.println(name + " show is registered !!");

    } else if (input.startsWith("onboardShowSlots:")) {
      onboardShowSlots(input.substring(17).trim());
    }
    else if (input.startsWith("showAvailByGenre:")) {
      showAvailByGenre(input.substring(17).trim());
    }
    else return input.startsWith("exit");
    return false;
  }

  private static void onboardShowSlots(String details) {
    String[] parts = details.split(" ");
    String eventName = parts[0];

    String[] slots = parts[1].split(", ");
    for (String slot : slots) {
      String[] slotParts = slot.split(" ");
      String[] times = slotParts[0].split("-");
      int capacity = Integer.parseInt(slotParts[1]);

      LocalTime startTime = LocalTime.parse(times[0], formatter);
      LocalTime endTime = startTime.plusHours(1);

      eventManager.addShow(eventName, startTime, endTime, capacity);
    }
    System.out.println("Done!");
  }

  private static void showAvailByGenre(String genre) {
    EventGenre eventGenre = EventGenre.getEventGenre(genre);
    eventManager.printShows(eventGenre);
  }

}
