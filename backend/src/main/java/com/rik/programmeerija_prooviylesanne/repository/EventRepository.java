package com.rik.programmeerija_prooviylesanne.repository;

import com.rik.programmeerija_prooviylesanne.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findAllByTimestampGreaterThan(LocalDateTime currentDateTime);
  List<Event> findAllByTimestampIsLessThanEqual(LocalDateTime currentDateTime);
}