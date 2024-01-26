package com.rik.programmeerija_prooviylesanne.service;

import com.rik.programmeerija_prooviylesanne.config.NotFoundException;
import com.rik.programmeerija_prooviylesanne.dto.CompanyDto;
import com.rik.programmeerija_prooviylesanne.model.Company;
import com.rik.programmeerija_prooviylesanne.repository.CompanyRepository;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyRepository companyRepository;
  private final EventRepository eventRepository;

  public CompanyDto company(Long id) {
    return companyRepository.findById(id).map(this::toCompanyDto).orElseThrow(this::companyNotFound);
  }

  public void updateCompany(Long id, CompanyDto dto) {
    Company company = companyRepository.findById(id).orElseThrow(this::companyNotFound);
    setCompanyAttributes(company, dto);
    companyRepository.save(company);
  }

  /**
   * Kui üritus on olemas, salvestatakse ettevõte ja lisatakse sündmusele.
   *
   * @param eventId ürituse ID, millele ettevõte lisatakse
   * @param dto ettevõtte andmete DTO
   */
  public void saveCompany(Long eventId, CompanyDto dto) {
    eventRepository.findById(eventId).ifPresent(event -> {
      Company company = new Company();
      setCompanyAttributes(company, dto);
      companyRepository.save(company);

      event.addCompany(company);
      eventRepository.save(event);
    });
  }

  private NotFoundException companyNotFound() {
    return new NotFoundException("Ettevõtte sellise ID-ga ei eksisteeri");
  }

  private void setCompanyAttributes(Company company, CompanyDto dto) {
    company.setName(dto.name());
    company.setRegistryCode(dto.registryCode());
    company.setParticipantsCount(dto.participantsCount());
    company.setPaymentType(dto.paymentType());
    company.setInfo(dto.info());
  }

  private CompanyDto toCompanyDto(Company company) {
    return new CompanyDto(
        company.getName(),
        company.getRegistryCode(),
        company.getParticipantsCount(),
        company.getPaymentType(),
        company.getInfo()
    );
  }
}
