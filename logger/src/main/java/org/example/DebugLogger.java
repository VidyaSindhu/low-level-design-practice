package org.example;

import lombok.extern.java.Log;

public class DebugLogger extends Logger {

  public DebugLogger(Logger logger) {
    super.logger = logger;
  }

  @Override
  void logMessage(LogLevel logLevel, String message) {
    if (logLevel == LogLevel.DEBUG) {
      System.out.print("Debug: " + message);
    }
    else {
      logger.logMessage(logLevel, message);
    }
  }
}
