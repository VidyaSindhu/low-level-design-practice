package org.example;

public class Application {
  public static void main(String[] args) {
    Logger logger = new InfoLogger(new DebugLogger(new ErrorLogger()));

    logger.logMessage(LogLevel.ERROR, "hey there");
  }
}
