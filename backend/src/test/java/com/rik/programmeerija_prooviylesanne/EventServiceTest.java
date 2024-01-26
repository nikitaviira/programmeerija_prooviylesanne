package com.rik.programmeerija_prooviylesanne;

import com.rik.programmeerija_prooviylesanne.config.NotFoundException;
import com.rik.programmeerija_prooviylesanne.config.ServiceException;
import com.rik.programmeerija_prooviylesanne.dto.EventDetailsDto;
import com.rik.programmeerija_prooviylesanne.dto.EventDisplayDto;
import com.rik.programmeerija_prooviylesanne.dto.EventParticipantDto;
import com.rik.programmeerija_prooviylesanne.model.Company;
import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.model.Person;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import com.rik.programmeerija_prooviylesanne.service.EventService;
import com.rik.programmeerija_prooviylesanne.util.DateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.rik.programmeerija_prooviylesanne.dto.ParticipantType.COMPANY;
import static com.rik.programmeerija_prooviylesanne.dto.ParticipantType.PERSON;
import static com.rik.programmeerija_prooviylesanne.model.PaymentType.CASH;
import static com.rik.programmeerija_prooviylesanne.util.DateUtil.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
  @Mock
  private EventRepository eventRepository;
  @InjectMocks
  private EventService eventService;

  @AfterAll
  static void afterAll() {
    DateUtil.resetMockNow();
    DateUtil.isTest = false;
  }

  @BeforeAll
  static void beforeAll() {
    DateUtil.isTest = true;
    DateUtil.setMockNow(Instant.now().minus(1, DAYS));
  }

  @Test
  void futureEvents() {
    when(eventRepository.findAllByTimestampGreaterThan(any())).thenReturn(events());

    assertThat(eventService.futureEvents()).containsExactly(
        new EventDisplayDto(3L, "Event 3", "23.01.2024"),
        new EventDisplayDto(1L, "Event 1", "22.01.2024"),
        new EventDisplayDto(2L, "Event 2", "19.01.2024")
    );
  }

  @Test
  void pastEvents() {
    when(eventRepository.findAllByTimestampIsLessThanEqual(any())).thenReturn(events());

    assertThat(eventService.pastEvents()).containsExactly(
        new EventDisplayDto(3L, "Event 3", "23.01.2024"),
        new EventDisplayDto(1L, "Event 1", "22.01.2024"),
        new EventDisplayDto(2L, "Event 2", "19.01.2024")
    );
  }

  @Test
  void eventDetails() {
    Long eventId = 1L;
    Event event = event();
    event.addPerson(person());
    event.addCompany(company());
    when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

    EventDetailsDto eventDetails = eventService.eventDetails(eventId);
    assertThat(eventDetails.id()).isEqualTo(1L);
    assertThat(eventDetails.name()).isEqualTo("Event 1");
    assertThat(eventDetails.datetime()).isEqualTo("22.01.2024 17:29");
    assertThat(eventDetails.place()).isEqualTo("Place 1");
    assertThat(eventDetails.participants()).containsExactly(
        new EventParticipantDto(1L, "Some name", "12345", COMPANY),
        new EventParticipantDto(1L, "Nikita Viira", "123123123", PERSON)
    );
  }

  @Test
  void eventDetails_throwsNotFoundException() {
    when(eventRepository.findById(any())).thenReturn(empty());
    assertThrows(NotFoundException.class, () -> eventService.eventDetails(1L), "Üritust sellise ID-ga ei eksisteeri");
  }

  @Test
  void removePersonFromEvent() {
    Event event = event();
    event.addPerson(person());
    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

    assertThat(event.getPersons()).hasSize(1);
    eventService.removePersonFromEvent(1L, 1L);
    assertThat(event.getPersons()).isEmpty();
  }

  @Test
  void removePersonFromEvent_throwsNotFoundException() {
    when(eventRepository.findById(1L)).thenReturn(empty());
    assertThrows(NotFoundException.class, () -> eventService.removePersonFromEvent(1L, 1L), "Üritust sellise ID-ga ei eksisteeri");
  }

  @Test
  void removeCompanyFromEvent() {
    Event event = event();
    event.addCompany(company());
    when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

    assertThat(event.getCompanies()).hasSize(1);
    eventService.removeCompanyFromEvent(1L, 1L);
    assertThat(event.getCompanies()).isEmpty();
  }

  @Test
  void removeCompanyFromEvent_throwsNotFoundException() {
    when(eventRepository.findById(1L)).thenReturn(empty());
    assertThrows(NotFoundException.class, () -> eventService.removeCompanyFromEvent(1L, 1L), "Üritust sellise ID-ga ei eksisteeri");
  }

  @Test
  void deleteEvent() {
    setMockNow(LocalDateTime.of(2024, 1, 22, 17, 25).atZone(TALLINN).toInstant());
    Long eventId = 1L;
    Event event = event();
    when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

    eventService.deleteEvent(eventId);

    verify(eventRepository).deleteById(eventId);
  }

  @Test
  void deleteEvent_cantDeletePastEvent() {
    Long eventId = 1L;
    Event event = event();
    when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
    assertThrows(ServiceException.class, () -> eventService.deleteEvent(eventId), "Ei saa kustuta toimunud üritust");
  }

  private Event event() {
    Event event = new Event();
    event.setId(1L);
    event.setName("Event 1");
    event.setTimestamp(parseLocalDateTime("22.01.2024 17:29:00"));
    event.setPlace("Place 1");
    event.setInfo("info 1");
    return event;
  }

  private List<Event> events() {
    Event e1 = new Event();
    e1.setId(1L);
    e1.setName("Event 1");
    e1.setTimestamp(parseLocalDateTime("22.01.2024 17:29:00"));
    e1.setPlace("Place 1");
    e1.setInfo("info 1");

    Event e2 = new Event();
    e2.setId(2L);
    e2.setName("Event 2");
    e2.setTimestamp(parseLocalDateTime("19.01.2024 17:29:00"));
    e2.setPlace("Place 2");
    e2.setInfo("info 2");

    Event e3 = new Event();
    e3.setId(3L);
    e3.setName("Event 3");
    e3.setTimestamp(parseLocalDateTime("23.01.2024 17:29:00"));
    e3.setPlace("Place 3");
    e3.setInfo("info 3");

    return List.of(e1, e2, e3);
  }

  private Company company() {
    Company company = new Company();
    company.setId(1L);
    company.setName("Some name");
    company.setPaymentType(CASH);
    company.setRegistryCode("12345");
    company.setParticipantsCount(15);
    company.setInfo("info");
    return company;
  }

  private Person person() {
    Person person = new Person();
    person.setId(1L);
    person.setFirstName("Nikita");
    person.setLastName("Viira");
    person.setPersonalCode("123123123");
    person.setPaymentType(CASH);
    person.setInfo("info");
    return person;
  }
}
