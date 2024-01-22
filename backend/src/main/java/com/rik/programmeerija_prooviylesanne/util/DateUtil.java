package com.rik.programmeerija_prooviylesanne.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.ofInstant;

public class DateUtil {
  public static final ZoneId TALLINN = ZoneId.of("Europe/Tallinn");

  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
      .ofPattern("dd.MM.yyyy")
      .withZone(TALLINN);

  static boolean isInsideMockRule;
  private static final ThreadLocal<Instant> mockNow = new ThreadLocal<>();

  public static void setMockNow(Instant now) {
    if (!isInsideMockRule) {
      throw new IllegalStateException();
    }
    mockNow.set(now);
  }

  public static void resetMockNow() {
    mockNow.remove();
  }

  public static Instant now() {
    Instant now = mockNow.get();
    return now != null ? now : Instant.now();
  }

  public static LocalDateTime nowLocalDateTime() {
    return ofInstant(now(), TALLINN);
  }

  public static String formatDate(LocalDateTime date) {
    return date.format(DATE_FORMATTER);
  }
}
