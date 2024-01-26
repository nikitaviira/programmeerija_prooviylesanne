package com.rik.programmeerija_prooviylesanne;

import com.rik.programmeerija_prooviylesanne.model.Company;
import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.model.Person;
import com.rik.programmeerija_prooviylesanne.repository.CompanyRepository;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import com.rik.programmeerija_prooviylesanne.repository.PersonRepository;
import com.rik.programmeerija_prooviylesanne.util.IntTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.rik.programmeerija_prooviylesanne.model.PaymentType.CASH;
import static com.rik.programmeerija_prooviylesanne.util.DateUtil.parseLocalDateTime;
import static com.rik.programmeerija_prooviylesanne.util.DateUtil.setMockNow;
import static com.rik.programmeerija_prooviylesanne.util.Util.dateTime;
import static com.rik.programmeerija_prooviylesanne.util.Util.event;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class EventControllerTest extends IntTestBase {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private EventRepository eventRepository;
  @Autowired
  private CompanyRepository companyRepository;
  @Autowired
  private PersonRepository personRepository;

  @Test
  public void saveEvent_success() throws Exception {
    setMockNow(dateTime("22.01.2024 17:00:00"));
    String event = "{\"name\": \"Sunnipaev\", \"timestamp\" : \"2024-01-22T17:01\", \"place\" : \"Mustamae tee 11\", \"info\" : \"\"}";
    mockMvc.perform(
            post("/api/events/save")
                .content(event)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());

    Optional<Event> eventOpt = eventRepository.findById(1L);
    assertThat(eventOpt).isPresent();
    assertThat(eventOpt.get().getId()).isEqualTo(1L);
    assertThat(eventOpt.get().getName()).isEqualTo("Sunnipaev");
    assertThat(eventOpt.get().getPlace()).isEqualTo("Mustamae tee 11");
    assertThat(eventOpt.get().getInfo()).isEqualTo("");
    assertThat(eventOpt.get().getTimestamp()).isEqualTo(parseLocalDateTime("22.01.2024 17:01:00"));
  }

  @Test
  public void saveEvent_validationFailed() throws Exception {
    String user = "{\"name\": \"\", \"timestamp\" : \"2024-01-20T17:01\", \"place\" : \"\", \"info\" : \"\"}";
    mockMvc.perform(
            post("/api/events/save")
                .content(user)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.fields", hasItem("Nimi on kohustuslik")))
        .andExpect(jsonPath("$.fields", hasItem("Koht on kohustuslik")))
        .andExpect(jsonPath("$.fields", hasItem("Toimumisaeg peab olema tulevikus")));

    assertThat(eventRepository.findAll()).isEmpty();
  }

  @Test
  public void futureEvents() throws Exception {
    setMockNow(dateTime("22.01.2024 17:30:00"));

    eventRepository.saveAllAndFlush(List.of(
        event("Event 1", parseLocalDateTime("22.01.2024 17:29:00"), "Place 1", ""),
        event("Event 2", parseLocalDateTime("22.01.2024 17:30:00"), "Place 2", ""),
        event("Event 3", parseLocalDateTime("23.01.2024 00:00:00"), "Place 3", ""),
        event("Event 4", parseLocalDateTime("24.01.2024 00:00:00"), "Place 4", ""))
    );

    mockMvc.perform(get("/api/events/future"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))

        .andExpect(jsonPath("$[0].id", is(3)))
        .andExpect(jsonPath("$[0].name", is("Event 3")))
        .andExpect(jsonPath("$[0].date", is("23.01.2024")))

        .andExpect(jsonPath("$[1].id", is(4)))
        .andExpect(jsonPath("$[1].name", is("Event 4")))
        .andExpect(jsonPath("$[1].date", is("24.01.2024")));

  }

  @Test
  public void pastEvents() throws Exception {
    setMockNow(dateTime("22.01.2024 17:30:00"));

    eventRepository.saveAllAndFlush(List.of(
        event("Event 1", parseLocalDateTime("22.01.2024 17:29:00"), "Place 1", ""),
        event("Event 2", parseLocalDateTime("22.01.2024 17:30:00"), "Place 2", ""),
        event("Event 3", parseLocalDateTime("23.01.2024 00:00:00"), "Place 3", ""),
        event("Event 4", parseLocalDateTime("24.01.2024 00:00:00"), "Place 4", ""))
    );

    mockMvc.perform(get("/api/events/past"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))

        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("Event 1")))
        .andExpect(jsonPath("$[0].date", is("22.01.2024")))

        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].name", is("Event 2")))
        .andExpect(jsonPath("$[1].date", is("22.01.2024")));

  }

  @Test
  public void eventDetails() throws Exception {
    var event = event("Event 1", parseLocalDateTime("22.01.2024 17:30:00"), "Place 1", "");
    eventRepository.save(event);

    var company = new Company();
    company.setName("Company 1");
    company.setRegistryCode("12345");
    company.setPaymentType(CASH);
    companyRepository.save(company);

    var person = new Person();
    person.setFirstName("Nikita");
    person.setLastName("Viira");
    person.setPersonalCode("123456");
    person.setPaymentType(CASH);
    personRepository.save(person);

    event.addCompany(company);
    event.addPerson(person);
    eventRepository.save(event);

    mockMvc.perform(get("/api/events/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Event 1")))
        .andExpect(jsonPath("$.datetime", is("22.01.2024 17:30")))
        .andExpect(jsonPath("$.place", is("Place 1")))
        .andExpect(jsonPath("$.participants", hasSize(2)))
        .andExpect(jsonPath("$.participants[0].id", is(1)))
        .andExpect(jsonPath("$.participants[0].fullName", is("Company 1")))
        .andExpect(jsonPath("$.participants[0].code", is("12345")))
        .andExpect(jsonPath("$.participants[0].participantType", is("COMPANY")))
        .andExpect(jsonPath("$.participants[1].id", is(1)))
        .andExpect(jsonPath("$.participants[1].fullName", is("Nikita Viira")))
        .andExpect(jsonPath("$.participants[1].code", is("123456")))
        .andExpect(jsonPath("$.participants[1].participantType", is("PERSON")));
  }

  @Test
  public void removePersonFromEvent() throws Exception {
    var event = event("Event 1", parseLocalDateTime("22.01.2024 17:30:00"), "Place 1", "");
    eventRepository.save(event);

    var person = new Person();
    person.setFirstName("Nikita");
    person.setLastName("Viira");
    person.setPersonalCode("123456");
    person.setPaymentType(CASH);
    personRepository.save(person);

    event.addPerson(person);
    eventRepository.save(event);

    Optional<Event> eventOpt = eventRepository.findById(1L);
    assertThat(eventOpt).isPresent();
    assertThat(eventOpt.get().getPersons()).hasSize(1);

    mockMvc.perform(delete("/api/events/1/person/1")).andExpect(status().isOk());

    eventOpt = eventRepository.findById(1L);
    assertThat(eventOpt).isPresent();
    assertThat(eventOpt.get().getPersons()).isEmpty();
  }

  @Test
  public void removeCompanyFromEvent() throws Exception {
    var event = event("Event 1", parseLocalDateTime("22.01.2024 17:30:00"), "Place 1", "");
    eventRepository.save(event);

    var company = new Company();
    company.setName("Company 1");
    company.setRegistryCode("12345");
    company.setPaymentType(CASH);
    companyRepository.save(company);

    event.addCompany(company);
    eventRepository.save(event);

    Optional<Event> eventOpt = eventRepository.findById(1L);
    assertThat(eventOpt).isPresent();
    assertThat(eventOpt.get().getCompanies()).hasSize(1);

    mockMvc.perform(delete("/api/events/1/company/1")).andExpect(status().isOk());

    eventOpt = eventRepository.findById(1L);
    assertThat(eventOpt).isPresent();
    assertThat(eventOpt.get().getCompanies()).isEmpty();
  }

  @Test
  public void deleteEvents() throws Exception {
    setMockNow(dateTime("01.01.2024 00:00:00"));
    eventRepository.saveAndFlush(event("Event 1", parseLocalDateTime("02.02.2024 00:00:00"), "Place 1", "some info"));
    assertThat(eventRepository.findAll()).hasSize(1);

    mockMvc.perform(delete("/api/events/1")).andExpect(status().isOk());

    assertThat(eventRepository.findAll()).isEmpty();
  }

  @Test
  public void deleteEvents_canNotDeleteEventInPast() throws Exception {
    eventRepository.saveAndFlush(event("Event 1", parseLocalDateTime("01.01.2024 00:00:00"), "Place 1", "some info"));
    assertThat(eventRepository.findAll()).hasSize(1);

    mockMvc.perform(delete("/api/events/1"))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Ei saa kustuta toimunud üritust")));

    assertThat(eventRepository.findAll()).hasSize(1);
  }
}
