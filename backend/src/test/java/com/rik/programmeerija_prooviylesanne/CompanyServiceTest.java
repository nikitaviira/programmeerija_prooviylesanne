package com.rik.programmeerija_prooviylesanne;

import com.rik.programmeerija_prooviylesanne.config.NotFoundException;
import com.rik.programmeerija_prooviylesanne.dto.CompanyDto;
import com.rik.programmeerija_prooviylesanne.model.Company;
import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.repository.CompanyRepository;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import com.rik.programmeerija_prooviylesanne.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.rik.programmeerija_prooviylesanne.model.PaymentType.BANK_TRANSFER;
import static com.rik.programmeerija_prooviylesanne.model.PaymentType.CASH;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
  @Mock
  private CompanyRepository companyRepository;
  @Mock
  private EventRepository eventRepository;
  @InjectMocks
  private CompanyService companyService;

  @Test
  void company() {
    Long companyId = 1L;
    var company = new Company();
    when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

    CompanyDto result = companyService.company(companyId);

    assertThat(result).isNotNull();
  }

  @Test
  void company_throwsNotFoundException() {
    Long companyId = 1L;
    when(companyRepository.findById(companyId)).thenReturn(empty());
    assertThrows(NotFoundException.class, () -> companyService.company(companyId));
  }

  @Test
  void updateCompany() {
    Long companyId = 1L;
    Company company = new Company();
    company.setName("Some name");
    company.setPaymentType(CASH);
    company.setRegistryCode("12345");
    company.setParticipantsCount(15);
    company.setInfo("info");

    CompanyDto dto = new CompanyDto("Some other name", "123123123", 10, BANK_TRANSFER, "some info");

    when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

    companyService.updateCompany(companyId, dto);

    ArgumentCaptor<Company> companyCaptor = forClass(Company.class);
    verify(companyRepository).save(companyCaptor.capture());

    Company capturedCompany = companyCaptor.getValue();
    assertThat(capturedCompany.getName()).isEqualTo("Some other name");
    assertThat(capturedCompany.getRegistryCode()).isEqualTo("123123123");
    assertThat(capturedCompany.getParticipantsCount()).isEqualTo(10);
    assertThat(capturedCompany.getPaymentType()).isEqualTo(BANK_TRANSFER);
    assertThat(capturedCompany.getInfo()).isEqualTo("some info");
  }

  @Test
  void updateCompany_throwsNotFoundException() {
    Long companyId = 1L;
    when(companyRepository.findById(companyId)).thenReturn(empty());
    assertThrows(NotFoundException.class,
        () -> companyService.updateCompany(companyId, new CompanyDto(null, null, null, null, null)));
  }

  @Test
  void saveCompany() {
    Long eventId = 1L;
    CompanyDto dto = new CompanyDto("Some other name", "123123123", 10, BANK_TRANSFER, "some info");
    when(eventRepository.findById(eventId)).thenReturn(Optional.of(new Event()));

    companyService.saveCompany(eventId, dto);

    ArgumentCaptor<Company> companyCaptor = forClass(Company.class);
    verify(companyRepository).save(companyCaptor.capture());

    Company capturedCompany = companyCaptor.getValue();
    assertThat(capturedCompany.getName()).isEqualTo("Some other name");
    assertThat(capturedCompany.getRegistryCode()).isEqualTo("123123123");
    assertThat(capturedCompany.getParticipantsCount()).isEqualTo(10);
    assertThat(capturedCompany.getPaymentType()).isEqualTo(BANK_TRANSFER);
    assertThat(capturedCompany.getInfo()).isEqualTo("some info");

    ArgumentCaptor<Event> eventCaptor = forClass(Event.class);
    verify(eventRepository).save(eventCaptor.capture());

    assertThat(eventCaptor.getValue().getCompanies()).contains(capturedCompany);
  }

  @Test
  void saveCompany_eventNotFound() {
    Long eventId = 1L;
    when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
    companyService.saveCompany(eventId, new CompanyDto(null, null, null, null, null));
    verify(companyRepository, never()).save(any());
  }
}
