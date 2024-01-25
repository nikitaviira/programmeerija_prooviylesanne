package com.rik.programmeerija_prooviylesanne.controller;

import com.rik.programmeerija_prooviylesanne.dto.CompanyDto;
import com.rik.programmeerija_prooviylesanne.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
  private final CompanyService companyService;

  @GetMapping("{id}")
  public CompanyDto company(@PathVariable Long id) {
    return companyService.company(id);
  }

  @PostMapping("save")
  public void saveCompany(@Valid @RequestBody CompanyDto company, @RequestParam Long eventId) {
    companyService.saveCompany(eventId, company);
  }

  @PutMapping("{id}/update")
  public void updateCompany(@Valid @RequestBody CompanyDto company, @PathVariable Long id) {
    companyService.updateCompany(id, company);
  }
}
