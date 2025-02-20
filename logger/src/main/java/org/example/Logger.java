package org.example;

import lombok.extern.java.Log;

public abstract class Logger {
  protected Logger logger;

  abstract void logMessage(LogLevel logLevel, String message);
}
