package enums;

public enum LockerSize {
  SMALL,
  MEDIUM,
  LARGE;

  public static LockerSize getLockerSize(OrderSize orderSize) {
    switch (orderSize) {
      case SMALL -> {
        return LockerSize.SMALL;
      }

      case MEDIUM -> {
        return LockerSize.MEDIUM;
      }

      default -> {
        return LockerSize.LARGE;
      }

    }
  }
}

