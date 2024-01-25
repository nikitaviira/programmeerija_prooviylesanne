package com.rik.programmeerija_prooviylesanne.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity
@Table(name = "person")
@ToString(exclude = {"events"})
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

    public String fullName() {
        return "%s %s".formatted(firstName, lastName);
    }
}
