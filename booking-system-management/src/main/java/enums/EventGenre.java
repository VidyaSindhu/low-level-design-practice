package enums;

public enum EventGenre {
  COMEDY,
  SINGING,
  THEATRE,
  TECH;

  public static EventGenre getEventGenre(String genre) {
    for (EventGenre eventGenre : EventGenre.values()) {
      if (eventGenre.name().equalsIgnoreCase(genre)) {
        return eventGenre;
      }
    }
    return null;
  }
}
