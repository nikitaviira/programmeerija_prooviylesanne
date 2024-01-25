package com.rik.programmeerija_prooviylesanne;

import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.model.Person;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import com.rik.programmeerija_prooviylesanne.repository.PersonRepository;
import com.rik.programmeerija_prooviylesanne.util.IntTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.rik.programmeerija_prooviylesanne.model.PaymentType.BANK_TRANSFER;
import static com.rik.programmeerija_prooviylesanne.model.PaymentType.CASH;
import static com.rik.programmeerija_prooviylesanne.util.DateUtil.parseLocalDateTime;
import static com.rik.programmeerija_prooviylesanne.util.Util.event;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class PersonControllerTest extends IntTestBase {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private EventRepository eventRepository;

  @Test
  public void savePerson() throws Exception {
    Event event = event("Event 1", parseLocalDateTime("22.01.2024 00:00:00"), "Place 1", "");
    eventRepository.save(event);

    String personJson = "{" +
        "\"firstName\": \"Nikita\", " +
        "\"lastName\": \"Viira\", " +
        "\"personalCode\" : \"39807093721\", " +
        "\"paymentType\" : \"CASH\", " +
        "\"info\" : \"some info\"}";

    mockMvc.perform(
            post("/api/person/save?eventId=1")
                .content(personJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());

    Optional<Person> personOpt = personRepository.findById(1L);
    assertThat(personOpt).isPresent();
    assertThat(personOpt.get().getId()).isEqualTo(1L);
    assertThat(personOpt.get().getFirstName()).isEqualTo("Nikita");
    assertThat(personOpt.get().getLastName()).isEqualTo("Viira");
    assertThat(personOpt.get().getPersonalCode()).isEqualTo("39807093721");
    assertThat(personOpt.get().getPaymentType()).isEqualTo(CASH);
    assertThat(personOpt.get().getInfo()).isEqualTo("some info");

    List<Event> events = personOpt.get().getEvents();
    assertThat(events).hasSize(1);
    assertThat(events.getFirst().getId()).isEqualTo(1L);
    assertThat(events.getFirst().getName()).isEqualTo("Event 1");
    assertThat(events.getFirst().getTimestamp()).isEqualTo(parseLocalDateTime("22.01.2024 00:00:00"));
    assertThat(events.getFirst().getPlace()).isEqualTo("Place 1");
  }

  @Test
  public void savePerson_validationFailed() throws Exception {
    String personJson = "{" +
        "\"firstName\": \"\", " +
        "\"lastName\": \"\", " +
        "\"personalCode\" : \"398070937222\", " +
        "\"paymentType\" : null, " +
        "\"info\" : \"" + "a".repeat(2000) + "\"}";

    mockMvc.perform(
            post("/api/person/save")
                .content(personJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.fields", hasItem("Eesnimi on kohustuslik")))
        .andExpect(jsonPath("$.fields", hasItem("Perenimi on kohustuslik")))
        .andExpect(jsonPath("$.fields", hasItem("Maksmise viis on kohustuslik")))
        .andExpect(jsonPath("$.fields", hasItem("Isikukood ei ole valiidne")))
        .andExpect(jsonPath("$.fields", hasItem("Maksimaalne lisainfo pikkus on 1500 sümbolid")));

    assertThat(personRepository.findAll()).isEmpty();
  }

  @Test
  public void updatePerson() throws Exception {
    addDefaultPerson();
    String updatedPersonJson = "{" +
        "\"firstName\": \"Vasja\", " +
        "\"lastName\": \"Petrov\", " +
        "\"personalCode\" : \"34501234215\", " +
        "\"paymentType\" : \"BANK_TRANSFER\", " +
        "\"info\" : \"some other info\"}";

    mockMvc.perform(
            put("/api/person/1/update")
                .content(updatedPersonJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());

    assertThat(personRepository.findAll()).hasSize(1);

    Optional<Person> personOpt = personRepository.findById(1L);
    assertThat(personOpt).isPresent();
    assertThat(personOpt.get().getId()).isEqualTo(1L);
    assertThat(personOpt.get().getFirstName()).isEqualTo("Vasja");
    assertThat(personOpt.get().getLastName()).isEqualTo("Petrov");
    assertThat(personOpt.get().getPersonalCode()).isEqualTo("34501234215");
    assertThat(personOpt.get().getPaymentType()).isEqualTo(BANK_TRANSFER);
    assertThat(personOpt.get().getInfo()).isEqualTo("some other info");
  }

  @Test
  public void updatePerson_wrongId() throws Exception {
    String updatedPersonJson = "{" +
        "\"firstName\": \"Vasja\", " +
        "\"lastName\": \"Petrov\", " +
        "\"personalCode\" : \"34501234215\", " +
        "\"paymentType\" : \"BANK_TRANSFER\", " +
        "\"info\" : \"some other info\"}";

    mockMvc.perform(
            put("/api/person/1/update")
                .content(updatedPersonJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Füsilist isiku sellise ID-ga ei eksisteeri")));
  }

  @Test
  public void person() throws Exception {
    addDefaultPerson();
    mockMvc.perform(get("/api/person/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName", is("Nikita")))
        .andExpect(jsonPath("$.lastName", is("Viira")))
        .andExpect(jsonPath("$.personalCode", is("39807093721")))
        .andExpect(jsonPath("$.paymentType", is("CASH")))
        .andExpect(jsonPath("$.info", is("some info")));
  }

  @Test
  public void person_wrongId() throws Exception {
    mockMvc.perform(get("/api/person/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Füsilist isiku sellise ID-ga ei eksisteeri")));
  }

  private void addDefaultPerson() {
    var person = new Person();
    person.setFirstName("Nikita");
    person.setLastName("Viira");
    person.setPersonalCode("39807093721");
    person.setPaymentType(CASH);
    person.setInfo("some info");
    personRepository.save(person);
  }
}
