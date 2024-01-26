package com.rik.programmeerija_prooviylesanne.controller.test;

import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import com.rik.programmeerija_prooviylesanne.util.EraseDbHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rik.programmeerija_prooviylesanne.util.DateUtil.parseLocalDateTime;

@RestController
@RequiredArgsConstructor
@Profile("uitest")
public class UITestController {
  private final EraseDbHelper eraseDbHelper;
  private final EventRepository eventRepository;

  @GetMapping("reset-db")
  public void resetDatabase() {
    eraseDbHelper.eraseDb();
  }

  @GetMapping("create-mock-event")
  public void createMockEvent() {
    Event mockEvent = new Event();
    mockEvent.setName("Sunnipaev");
    mockEvent.setTimestamp(parseLocalDateTime("22.01.2024 17:01:00"));
    mockEvent.setPlace("Mustamae tee 11");
    eventRepository.save(mockEvent);
  }
}
