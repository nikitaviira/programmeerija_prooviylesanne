package com.rik.programmeerija_prooviylesanne;

import com.rik.programmeerija_prooviylesanne.model.Company;
import com.rik.programmeerija_prooviylesanne.repository.CompanyRepository;
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
public class CompanyControllerTest extends IntTestBase {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CompanyRepository companyRepository;

  @Test
  public void saveCompany_new() throws Exception {
    String companyJson = "{" +
        "\"name\": \"Ettevotte 1\", " +
        "\"registryCode\" : \"14812701\", " +
        "\"participantsCount\" : 10, " +
        "\"paymentType\" : \"CASH\", " +
        "\"info\" : \"some info\"}";

    mockMvc.perform(
            post("/api/company/save")
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
  }

  @Test
  public void saveCompany_updateExisting() throws Exception {
    addDefaultCompany();
    String updatedCompanyJson = "{" +
        "\"id\": 1, " +
        "\"name\": \"Ettevotte 2\", " +
        "\"registryCode\" : \"10000640\", " +
        "\"participantsCount\" : 11, " +
        "\"paymentType\" : \"BANK_TRANSFER\", " +
        "\"info\" : \"some other info\"}";

    mockMvc.perform(
            post("/api/company/save")
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
  public void saveCompany_updateExisting_wrongId() throws Exception {
    addDefaultCompany();
    String updatedCompanyJson = "{" +
        "\"id\": 2, " +
        "\"name\": \"Ettevotte 2\", " +
        "\"registryCode\" : \"10000640\", " +
        "\"participantsCount\" : 11, " +
        "\"paymentType\" : \"BANK_TRANSFER\", " +
        "\"info\" : \"some other info\"}";

    mockMvc.perform(
            post("/api/company/save")
                .content(updatedCompanyJson)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Ettevõtte sellise ID-ga ei eksisteeri")));
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
  public void company() throws Exception {
    addDefaultCompany();
    mockMvc.perform(get("/api/company/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
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