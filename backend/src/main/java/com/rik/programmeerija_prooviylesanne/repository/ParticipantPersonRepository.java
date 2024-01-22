package com.rik.programmeerija_prooviylesanne.repository;

import com.rik.programmeerija_prooviylesanne.model.ParticipantPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantPersonRepository extends JpaRepository<ParticipantPerson, Long> {
}