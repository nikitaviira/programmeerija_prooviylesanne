package com.rik.programmeerija_prooviylesanne.service;

import com.rik.programmeerija_prooviylesanne.config.NotFoundException;
import com.rik.programmeerija_prooviylesanne.dto.PersonDto;
import com.rik.programmeerija_prooviylesanne.model.Person;
import com.rik.programmeerija_prooviylesanne.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
  private final PersonRepository personRepository;

  public PersonDto person(Long id) {
    return personRepository.findById(id).map(this::toPersonDto).orElseThrow(this::personNotFound);
  }

  public void savePerson(PersonDto dto) {
    Person person = getOrCreatePerson(dto);
    person.setFirstName(dto.firstName());
    person.setLastName(dto.lastName());
    person.setPersonalCode(dto.personalCode());
    person.setPaymentType(dto.paymentType());
    person.setInfo(dto.info());
    personRepository.save(person);
  }

  private Person getOrCreatePerson(PersonDto dto) {
    return dto.id() == null ? new Person() : personRepository.findById(dto.id()).orElseThrow(this::personNotFound);
  }

  private NotFoundException personNotFound() {
    return new NotFoundException("Füsilist isiku sellise ID-ga ei eksisteeri");
  }

  private PersonDto toPersonDto(Person person) {
    return new PersonDto(
        person.getId(),
        person.getFirstName(),
        person.getLastName(),
        person.getPersonalCode(),
        person.getPaymentType(),
        person.getInfo()
    );
  }
}
