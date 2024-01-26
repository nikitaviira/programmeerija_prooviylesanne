package com.rik.programmeerija_prooviylesanne;

import com.rik.programmeerija_prooviylesanne.config.NotFoundException;
import com.rik.programmeerija_prooviylesanne.dto.PersonDto;
import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.model.Person;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import com.rik.programmeerija_prooviylesanne.repository.PersonRepository;
import com.rik.programmeerija_prooviylesanne.service.PersonService;
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
class PersonServiceTest {
  @Mock
  private PersonRepository personRepository;
  @Mock
  private EventRepository eventRepository;
  @InjectMocks
  private PersonService personService;

  @Test
  void person() {
    Long personId = 1L;
    var person = new Person();
    person.setFirstName("Nikita");
    person.setLastName("Viira");
    person.setPersonalCode("123123123");
    person.setPaymentType(CASH);
    person.setInfo("info");
    when(personRepository.findById(personId)).thenReturn(Optional.of(person));

    PersonDto result = personService.person(personId);

    assertThat(result.firstName()).isEqualTo("Nikita");
    assertThat(result.lastName()).isEqualTo("Viira");
    assertThat(result.personalCode()).isEqualTo("123123123");
    assertThat(result.paymentType()).isEqualTo(CASH);
    assertThat(result.info()).isEqualTo("info");
  }

  @Test
  void person_throwsNotFoundException() {
    Long personId = 1L;
    when(personRepository.findById(personId)).thenReturn(empty());
    assertThrows(NotFoundException.class, () -> personService.person(personId), "Füsilist isiku sellise ID-ga ei eksisteeri");
  }

  @Test
  void updatePerson() {
    Long personId = 1L;
    var person = new Person();
    person.setFirstName("Nikita");
    person.setLastName("Viira");
    person.setPersonalCode("123123123");
    person.setPaymentType(CASH);
    person.setInfo("info");

    PersonDto dto = new PersonDto("Ivan", "Ivanovic", "12345", BANK_TRANSFER, "some info");

    when(personRepository.findById(personId)).thenReturn(Optional.of(person));

    personService.updatePerson(personId, dto);

    ArgumentCaptor<Person> personCaptor = forClass(Person.class);
    verify(personRepository).save(personCaptor.capture());

    Person capturedperson = personCaptor.getValue();
    assertThat(capturedperson.getFirstName()).isEqualTo("Ivan");
    assertThat(capturedperson.getLastName()).isEqualTo("Ivanovic");
    assertThat(capturedperson.getPersonalCode()).isEqualTo("12345");
    assertThat(capturedperson.getPaymentType()).isEqualTo(BANK_TRANSFER);
    assertThat(capturedperson.getInfo()).isEqualTo("some info");
  }

  @Test
  void updatePerson_throwsNotFoundException() {
    Long personId = 1L;
    when(personRepository.findById(personId)).thenReturn(empty());
    assertThrows(NotFoundException.class,
        () -> personService.updatePerson(personId, new PersonDto(null, null, null, null, null)),
        "Füsilist isiku sellise ID-ga ei eksisteeri");
  }

  @Test
  void savePerson() {
    Long eventId = 1L;
    PersonDto dto = new PersonDto("Ivan", "Ivanovic", "12345", BANK_TRANSFER, "some info");
    when(eventRepository.findById(eventId)).thenReturn(Optional.of(new Event()));

    personService.savePerson(eventId, dto);

    ArgumentCaptor<Person> personCaptor = forClass(Person.class);
    verify(personRepository).save(personCaptor.capture());

    Person capturedPerson = personCaptor.getValue();
    assertThat(capturedPerson.getFirstName()).isEqualTo("Ivan");
    assertThat(capturedPerson.getLastName()).isEqualTo("Ivanovic");
    assertThat(capturedPerson.getPersonalCode()).isEqualTo("12345");
    assertThat(capturedPerson.getPaymentType()).isEqualTo(BANK_TRANSFER);
    assertThat(capturedPerson.getInfo()).isEqualTo("some info");

    ArgumentCaptor<Event> eventCaptor = forClass(Event.class);
    verify(eventRepository).save(eventCaptor.capture());

    assertThat(eventCaptor.getValue().getPersons()).contains(capturedPerson);
  }

  @Test
  void savePerson_eventNotFound() {
    Long eventId = 1L;
    when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
    personService.savePerson(eventId, new PersonDto(null, null, null, null, null));
    verify(personRepository, never()).save(any());
  }
}
