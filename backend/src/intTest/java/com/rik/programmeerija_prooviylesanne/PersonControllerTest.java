package com.rik.programmeerija_prooviylesanne;

import com.rik.programmeerija_prooviylesanne.model.Person;
import com.rik.programmeerija_prooviylesanne.repository.PersonRepository;
import com.rik.programmeerija_prooviylesanne.util.IntTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.rik.programmeerija_prooviylesanne.model.PaymentType.BANK_TRANSFER;
import static com.rik.programmeerija_prooviylesanne.model.PaymentType.CASH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class PersonControllerTest extends IntTestBase {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PersonRepository personRepository;

  @Test
  public void savePerson_new() throws Exception {
    String personJson = "{" +
        "\"firstName\": \"Nikita\", " +
        "\"lastName\": \"Viira\", " +
        "\"personalCode\" : \"39807093721\", " +
        "\"paymentType\" : \"CASH\", " +
        "\"info\" : \"some info\"}";

    mockMvc.perform(
            post("/api/person/save")
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
  }

  @Test
  public void savePerson_updateExisting() throws Exception {
    addDefaultPerson();
    String updatedPersonJson = "{" +
        "\"id\": \"1\", " +
        "\"firstName\": \"Vasja\", " +
        "\"lastName\": \"Petrov\", " +
        "\"personalCode\" : \"34501234215\", " +
        "\"paymentType\" : \"BANK_TRANSFER\", " +
        "\"info\" : \"some other info\"}";

    mockMvc.perform(
            post("/api/person/save")
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
  public void savePerson_updateExisting_wrongId() throws Exception {
    addDefaultPerson();
    String updatedPersonJson = "{" +
        "\"id\": \"2\", " +
        "\"firstName\": \"Vasja\", " +
        "\"lastName\": \"Petrov\", " +
        "\"personalCode\" : \"34501234215\", " +
        "\"paymentType\" : \"BANK_TRANSFER\", " +
        "\"info\" : \"some other info\"}";

    mockMvc.perform(
            post("/api/person/save")
                .content(updatedPersonJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Füsilist isiku sellise ID-ga ei eksisteeri")));
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
  public void person() throws Exception {
    addDefaultPerson();
    mockMvc.perform(get("/api/person/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
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
