package com.rik.programmeerija_prooviylesanne.service;

import com.rik.programmeerija_prooviylesanne.config.NotFoundException;
import com.rik.programmeerija_prooviylesanne.dto.CompanyDto;
import com.rik.programmeerija_prooviylesanne.model.Company;
import com.rik.programmeerija_prooviylesanne.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyRepository companyRepository;

  public CompanyDto company(Long id) {
    return companyRepository.findById(id).map(this::toCompanyDto).orElseThrow(this::companyNotFound);
  }

  public void saveCompany(CompanyDto dto) {
    Company company = getOrCreateCompany(dto);
    company.setName(dto.name());
    company.setRegistryCode(dto.registryCode());
    company.setParticipantsCount(dto.participantsCount());
    company.setPaymentType(dto.paymentType());
    company.setInfo(dto.info());
    companyRepository.save(company);
  }

  private Company getOrCreateCompany(CompanyDto dto) {
    return dto.id() == null ? new Company() : companyRepository.findById(dto.id()).orElseThrow(this::companyNotFound);
  }

  private NotFoundException companyNotFound() {
    return new NotFoundException("Ettevõtte sellise ID-ga ei eksisteeri");
  }

  private CompanyDto toCompanyDto(Company company) {
    return new CompanyDto(
        company.getId(),
        company.getName(),
        company.getRegistryCode(),
        company.getParticipantsCount(),
        company.getPaymentType(),
        company.getInfo()
    );
  }
}
