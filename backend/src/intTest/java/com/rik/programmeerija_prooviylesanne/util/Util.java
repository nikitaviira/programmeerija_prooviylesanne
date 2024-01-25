package com.rik.programmeerija_prooviylesanne.util;

import com.rik.programmeerija_prooviylesanne.model.Event;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.rik.programmeerija_prooviylesanne.util.DateUtil.parseDateTime;

public class Util {
  public static Instant dateTime(String ddMmYyyyHhMmSs) {
    return parseDateTime(ddMmYyyyHhMmSs);
  }

  public static Event event(String name, LocalDateTime timestamp, String place, String info) {
    Event event = new Event();
    event.setName(name);
    event.setTimestamp(timestamp);
    event.setPlace(place);
    event.setInfo(info);
    return event;
  }
}
