package org.example;

public class InfoLogger extends Logger {

  public InfoLogger(Logger logger) {
    super.logger = logger;
  }

  @Override
  void logMessage(LogLevel logLevel, String message) {
    if (LogLevel.INFO == logLevel) {
      System.out.print("Info: " + message);
    }
    else {
      logger.logMessage(logLevel, message);
    }
  }
}
