package com.rik.programmeerija_prooviylesanne;

import com.rik.programmeerija_prooviylesanne.model.Event;
import com.rik.programmeerija_prooviylesanne.repository.EventRepository;
import com.rik.programmeerija_prooviylesanne.util.IntTestBase;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest extends IntTestBase {
    @Autowired
    EventRepository eventRepository;

    @Test
    @Order(1)
    void idGeneratedOnSave(){
        Event event = new Event();
        eventRepository.saveAndFlush(event);
        assertThat(event.getId()).isNotNull();
    }

    @Test
    @Order(2)
    void dbIsErased(){
        List<Event> all = eventRepository.findAll();
        assertThat(all).isEmpty();
    }
}