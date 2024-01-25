package com.rik.programmeerija_prooviylesanne.controller;

import com.rik.programmeerija_prooviylesanne.dto.PersonDto;
import com.rik.programmeerija_prooviylesanne.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {
  private final PersonService personService;

  @GetMapping("{id}")
  public PersonDto person(@PathVariable Long id) {
    return personService.person(id);
  }

  @PostMapping("save")
  public void savePerson(@Valid @RequestBody PersonDto person, @RequestParam Long eventId) {
    personService.savePerson(eventId, person);
  }

  @PutMapping("{id}/update")
  public void updatePerson(@Valid @RequestBody PersonDto person, @PathVariable Long id) {
    personService.updatePerson(id, person);
  }
}
