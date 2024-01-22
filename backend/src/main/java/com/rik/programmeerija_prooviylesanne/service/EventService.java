package com.rik.programmeerija_prooviylesanne.service;

import com.rik.programmeerija_prooviylesanne.dto.EventDto;
import com.rik.programmeerija_prooviylesanne.dto.SaveEventDto;
import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rik.programmeerija_prooviylesanne.util.DateUtil.formatDate;

@Service
@RequiredArgsConstructor
public class EventService {
  private final EventRepository eventRepository;

  public List<EventDto> allEvents() {
    return eventRepository.findAll().stream()
        .map(e -> new EventDto(e.getId(), e.getName(), formatDate(e.getTimestamp())))
        .toList();
  }

  public void saveEvent(SaveEventDto dto) {
    var event = new Event();
    event.setName(dto.name());
    event.setPlace(dto.place());
    event.setTimestamp(dto.timestamp());
    event.setAdditionalInfo(dto.info());
    eventRepository.save(event);
  }

  public void deleteEvent(Long id) {
    eventRepository.deleteById(id);
  }
}
