package com.rik.programmeerija_prooviylesanne.repository;

import com.rik.programmeerija_prooviylesanne.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}