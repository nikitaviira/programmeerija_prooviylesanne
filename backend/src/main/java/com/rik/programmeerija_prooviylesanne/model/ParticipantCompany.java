package com.rik.programmeerija_prooviylesanne.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity
@Table(name = "participant_companies")
public class ParticipantCompany {
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

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
