package com.rik.programmeerija_prooviylesanne.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private Instant timestamp;
    private String place;
    @Column(name = "additional_info")
    private String additionalInfo;

    @OneToMany(mappedBy = "event", cascade = ALL, orphanRemoval = true)
    private List<ParticipantCompany> participantCompanies;

    @OneToMany(mappedBy = "event", cascade = ALL, orphanRemoval = true)
    private List<ParticipantPerson> participantPersons;
}
