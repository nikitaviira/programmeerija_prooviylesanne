package com.rik.programmeerija_prooviylesanne.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "personal_code")
    private String personalCode;
    @Column(name = "payment_type")
    private PaymentType paymentType;
    private String info;

    @ManyToMany(mappedBy = "persons")
    private List<Event> events;
}
