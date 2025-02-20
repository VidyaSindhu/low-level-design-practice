package org.example;

public class ErrorLogger extends Logger {
  @Override
  void logMessage(LogLevel logLevel, String message) {
    if (logLevel == LogLevel.ERROR) {
      System.out.print("Error: " + message);
    }
  }
}
