package com.rik.programmeerija_prooviylesanne.util;

import java.time.Instant;

import static com.rik.programmeerija_prooviylesanne.util.DateUtil.parseDateTime;

public class Util {
  public static Instant dateTime(String ddMmYyyyHhMmSs) {
    return parseDateTime(ddMmYyyyHhMmSs);
  }
}
