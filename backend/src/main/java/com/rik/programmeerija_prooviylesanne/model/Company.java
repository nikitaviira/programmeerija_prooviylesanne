package com.rik.programmeerija_prooviylesanne.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    @Column(name = "registry_code")
    private String registryCode;
    @Column(name = "participants_count")
    private int participantsCount;
    @Column(name = "payment_type")
    private PaymentType paymentType;
    private String info;

    @ManyToMany(mappedBy = "companies")
    private List<Event> events;
}
