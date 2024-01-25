package com.rik.programmeerija_prooviylesanne.service;

import com.rik.programmeerija_prooviylesanne.config.NotFoundException;
import com.rik.programmeerija_prooviylesanne.dto.PersonDto;
import com.rik.programmeerija_prooviylesanne.model.Person;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import com.rik.programmeerija_prooviylesanne.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
  private final PersonRepository personRepository;
  private final EventRepository eventRepository;

  public PersonDto person(Long id) {
    return personRepository.findById(id).map(this::toPersonDto).orElseThrow(this::personNotFound);
  }

  public void updatePerson(Long id, PersonDto dto) {
    Person person = personRepository.findById(id).orElseThrow(this::personNotFound);
    setPersonAttributes(person, dto);
    personRepository.save(person);
  }

  public void savePerson(Long eventId, PersonDto dto) {
    Person person = new Person();
    setPersonAttributes(person, dto);
    personRepository.save(person);

    eventRepository.findById(eventId).ifPresent(event -> {
      event.addPerson(person);
      eventRepository.save(event);
    });
  }

  private NotFoundException personNotFound() {
    return new NotFoundException("Füsilist isiku sellise ID-ga ei eksisteeri");
  }

  private void setPersonAttributes(Person person, PersonDto dto) {
    person.setFirstName(dto.firstName());
    person.setLastName(dto.lastName());
    person.setPersonalCode(dto.personalCode());
    person.setPaymentType(dto.paymentType());
    person.setInfo(dto.info());
  }

  private PersonDto toPersonDto(Person person) {
    return new PersonDto(
        person.getFirstName(),
        person.getLastName(),
        person.getPersonalCode(),
        person.getPaymentType(),
        person.getInfo()
    );
  }
}
