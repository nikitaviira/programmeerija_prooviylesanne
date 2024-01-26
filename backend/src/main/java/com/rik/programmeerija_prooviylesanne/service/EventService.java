package com.rik.programmeerija_prooviylesanne.service;

import com.rik.programmeerija_prooviylesanne.config.NotFoundException;
import com.rik.programmeerija_prooviylesanne.config.ServiceException;
import com.rik.programmeerija_prooviylesanne.dto.EventDetailsDto;
import com.rik.programmeerija_prooviylesanne.dto.EventDisplayDto;
import com.rik.programmeerija_prooviylesanne.dto.EventParticipantDto;
import com.rik.programmeerija_prooviylesanne.dto.SaveEventDto;
import com.rik.programmeerija_prooviylesanne.model.Company;
import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.model.Person;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.rik.programmeerija_prooviylesanne.dto.ParticipantType.COMPANY;
import static com.rik.programmeerija_prooviylesanne.dto.ParticipantType.PERSON;
import static com.rik.programmeerija_prooviylesanne.util.DateUtil.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class EventService {
  private final EventRepository eventRepository;

  public List<EventDisplayDto> futureEvents() {
    LocalDateTime currentDateTime = nowLocalDateTime();
    return eventRepository.findAllByTimestampGreaterThan(currentDateTime).stream()
        .sorted(comparing(Event::getTimestamp).reversed())
        .map(this::mapToEventDto)
        .toList();
  }

  public List<EventDisplayDto> pastEvents() {
    LocalDateTime currentDateTime = nowLocalDateTime();
    return eventRepository.findAllByTimestampIsLessThanEqual(currentDateTime).stream()
        .sorted(comparing(Event::getTimestamp).reversed())
        .map(this::mapToEventDto)
        .toList();
  }

  public EventDetailsDto eventDetails(Long id) {
    Event event = findEventOrThrow(id);
    List<EventParticipantDto> participants = concat(
        event.getCompanies().stream()
            .map(c -> new EventParticipantDto(c.getId(), c.getName(), c.getRegistryCode(), COMPANY)),
        event.getPersons().stream()
            .map(c -> new EventParticipantDto(c.getId(), c.fullName(), c.getPersonalCode(), PERSON))
    ).toList();

    return new EventDetailsDto(
        event.getId(),
        event.getName(),
        formatDateTimeShort(event.getTimestamp()),
        event.getPlace(),
        participants
    );
  }

  public void removePersonFromEvent(Long id, Long personId) {
    Event event = findEventOrThrow(id);
    List<Person> persons = event.getPersons();
    persons.removeIf(person -> person.getId().equals(personId));
    eventRepository.save(event);
  }

  public void removeCompanyFromEvent(Long id, Long companyId) {
    Event event = findEventOrThrow(id);
    List<Company> companies = event.getCompanies();
    companies.removeIf(company -> company.getId().equals(companyId));
    eventRepository.save(event);
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

  private Event findEventOrThrow(Long id) {
    return eventRepository.findById(id).orElseThrow(this::eventNotFound);
  }

  private NotFoundException eventNotFound() {
    return new NotFoundException("Üritust sellise ID-ga ei eksisteeri");
  }

  private EventDisplayDto mapToEventDto(Event event) {
    return new EventDisplayDto(event.getId(), event.getName(), formatDate(event.getTimestamp()));
  }
}
