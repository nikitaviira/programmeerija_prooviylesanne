package com.rik.programmeerija_prooviylesanne.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private LocalDateTime timestamp;
    private String place;
    private String info;

    @ManyToMany(cascade = PERSIST)
    @JoinTable(
        name = "event_company",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private List<Company> companies = new ArrayList<>();

    @ManyToMany(cascade = PERSIST)
    @JoinTable(
        name = "event_person",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> persons = new ArrayList<>();

    public void addCompany(Company company) {
        this.companies.add(company);
    }

    public void addPerson(Person person) {
        this.persons.add(person);
    }
}
