package com.rik.programmeerija_prooviylesanne;

import com.rik.programmeerija_prooviylesanne.model.Company;
import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.repository.CompanyRepository;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
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
public class CompanyControllerTest extends IntTestBase {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CompanyRepository companyRepository;
  @Autowired
  private EventRepository eventRepository;

  @Test
  public void saveCompany() throws Exception {
    Event event = event("Event 1", parseLocalDateTime("22.01.2024 00:00:00"), "Place 1", "");
    eventRepository.save(event);

    String companyJson = "{" +
        "\"name\": \"Ettevotte 1\", " +
        "\"registryCode\" : \"14812701\", " +
        "\"participantsCount\" : 10, " +
        "\"paymentType\" : \"CASH\", " +
        "\"info\" : \"some info\"}";

    mockMvc.perform(
            post("/api/company/save?eventId=1")
                .content(companyJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());

    Optional<Company> companyOpt = companyRepository.findById(1L);
    assertThat(companyOpt).isPresent();
    assertThat(companyOpt.get().getId()).isEqualTo(1L);
    assertThat(companyOpt.get().getName()).isEqualTo("Ettevotte 1");
    assertThat(companyOpt.get().getRegistryCode()).isEqualTo("14812701");
    assertThat(companyOpt.get().getParticipantsCount()).isEqualTo(10);
    assertThat(companyOpt.get().getPaymentType()).isEqualTo(CASH);
    assertThat(companyOpt.get().getInfo()).isEqualTo("some info");

    List<Event> events = companyOpt.get().getEvents();
    assertThat(events).hasSize(1);
    assertThat(events.getFirst().getId()).isEqualTo(1L);
    assertThat(events.getFirst().getName()).isEqualTo("Event 1");
    assertThat(events.getFirst().getTimestamp()).isEqualTo(parseLocalDateTime("22.01.2024 00:00:00"));
    assertThat(events.getFirst().getPlace()).isEqualTo("Place 1");
  }

  @Test
  public void saveCompany_validationFailed() throws Exception {
    String companyJson = "{" +
        "\"name\": \"\", " +
        "\"registryCode\" : \"invalid-code\", " +
        "\"participantsCount\" : 1000, " +
        "\"paymentType\" : null, " +
        "\"info\" : \"" + "a".repeat(5500) + "\"}";

    mockMvc.perform(
            post("/api/company/save")
                .content(companyJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.fields", hasItem("Maksmise viis on kohustuslik")))
        .andExpect(jsonPath("$.fields", hasItem("Ettevõtte registrikood ei ole valiidne")))
        .andExpect(jsonPath("$.fields", hasItem("Nimi on kohustuslik")))
        .andExpect(jsonPath("$.fields", hasItem("Maksimaalne lisainfo pikkus on 5000 sümbolid")))
        .andExpect(jsonPath("$.fields", hasItem("Osavõtjate arv peab olema 0 ja 100 vahel")));

    assertThat(companyRepository.findAll()).isEmpty();
  }

  @Test
  public void updateCompany() throws Exception {
    addDefaultCompany();
    String updatedCompanyJson = "{" +
        "\"name\": \"Ettevotte 2\", " +
        "\"registryCode\" : \"10000640\", " +
        "\"participantsCount\" : 11, " +
        "\"paymentType\" : \"BANK_TRANSFER\", " +
        "\"info\" : \"some other info\"}";

    mockMvc.perform(
            put("/api/company/1/update")
                .content(updatedCompanyJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());

    assertThat(companyRepository.findAll()).hasSize(1);

    Optional<Company> companyOpt = companyRepository.findById(1L);
    assertThat(companyOpt).isPresent();
    assertThat(companyOpt.get().getId()).isEqualTo(1L);
    assertThat(companyOpt.get().getName()).isEqualTo("Ettevotte 2");
    assertThat(companyOpt.get().getRegistryCode()).isEqualTo("10000640");
    assertThat(companyOpt.get().getParticipantsCount()).isEqualTo(11);
    assertThat(companyOpt.get().getPaymentType()).isEqualTo(BANK_TRANSFER);
    assertThat(companyOpt.get().getInfo()).isEqualTo("some other info");
  }

  @Test
  public void updateCompany_wrongId() throws Exception {
    String updatedCompanyJson = "{" +
        "\"name\": \"Ettevotte 2\", " +
        "\"registryCode\" : \"10000640\", " +
        "\"participantsCount\" : 11, " +
        "\"paymentType\" : \"BANK_TRANSFER\", " +
        "\"info\" : \"some other info\"}";

    mockMvc.perform(
            put("/api/company/1/update")
                .content(updatedCompanyJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Ettevõtte sellise ID-ga ei eksisteeri")));
  }

  @Test
  public void company() throws Exception {
    addDefaultCompany();
    mockMvc.perform(get("/api/company/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("Ettevotte 1")))
        .andExpect(jsonPath("$.registryCode", is("14812701")))
        .andExpect(jsonPath("$.participantsCount", is(10)))
        .andExpect(jsonPath("$.paymentType", is("CASH")))
        .andExpect(jsonPath("$.info", is("some info")));
  }

  @Test
  public void company_wrongId() throws Exception {
    mockMvc.perform(get("/api/company/1"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Ettevõtte sellise ID-ga ei eksisteeri")));
  }

  private void addDefaultCompany() {
    var existingCompany = new Company();
    existingCompany.setName("Ettevotte 1");
    existingCompany.setRegistryCode("14812701");
    existingCompany.setParticipantsCount(10);
    existingCompany.setPaymentType(CASH);
    existingCompany.setInfo("some info");
    companyRepository.save(existingCompany);
  }
}