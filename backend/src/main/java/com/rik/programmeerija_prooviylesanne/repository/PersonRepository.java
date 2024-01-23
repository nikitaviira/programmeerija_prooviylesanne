package com.rik.programmeerija_prooviylesanne.repository;

import com.rik.programmeerija_prooviylesanne.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}