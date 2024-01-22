package com.rik.programmeerija_prooviylesanne.repository;

import com.rik.programmeerija_prooviylesanne.model.ParticipantCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyParticipantRepository extends JpaRepository<ParticipantCompany, Long> {
}