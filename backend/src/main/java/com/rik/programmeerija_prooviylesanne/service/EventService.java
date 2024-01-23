package com.rik.programmeerija_prooviylesanne.service;

import com.rik.programmeerija_prooviylesanne.config.ServiceException;
import com.rik.programmeerija_prooviylesanne.dto.EventDto;
import com.rik.programmeerija_prooviylesanne.dto.SaveEventDto;
import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.rik.programmeerija_prooviylesanne.util.DateUtil.formatDate;
import static com.rik.programmeerija_prooviylesanne.util.DateUtil.nowLocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {
  private final EventRepository eventRepository;

  public List<EventDto> futureEvents() {
    LocalDateTime currentDateTime = nowLocalDateTime();
    return eventRepository.findAllByTimestampGreaterThan(currentDateTime).stream().map(this::mapToEventDto).toList();
  }

  public List<EventDto> pastEvents() {
    LocalDateTime currentDateTime = nowLocalDateTime();
    return eventRepository.findAllByTimestampIsLessThanEqual(currentDateTime).stream().map(this::mapToEventDto).toList();
  }

  public void saveEvent(SaveEventDto dto) {
    var event = new Event();
    event.setName(dto.name());
    event.setPlace(dto.place());
    event.setTimestamp(dto.timestamp());
    event.setInfo(dto.info());
    eventRepository.save(event);
  }

  public void deleteEvent(Long id) {
    eventRepository.findById(id).ifPresent(event -> {
      if (event.getTimestamp().isBefore(nowLocalDateTime())) {
        throw new ServiceException("Ei saa kustuta toimunud üritust");
      }

      eventRepository.deleteById(id);
    });
  }

  private EventDto mapToEventDto(Event event) {
    return new EventDto(event.getId(), event.getName(), formatDate(event.getTimestamp()));
  }
}
