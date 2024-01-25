package com.rik.programmeerija_prooviylesanne.controller;

import com.rik.programmeerija_prooviylesanne.dto.EventDetailsDto;
import com.rik.programmeerija_prooviylesanne.dto.EventDisplayDto;
import com.rik.programmeerija_prooviylesanne.dto.SaveEventDto;
import com.rik.programmeerija_prooviylesanne.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("future")
    public List<EventDisplayDto> futureEvents() {
        return eventService.futureEvents();
    }

    @GetMapping("past")
    public List<EventDisplayDto> pastEvents() {
        return eventService.pastEvents();
    }

    @GetMapping("{id}")
    public EventDetailsDto eventDetails(@PathVariable Long id) {
        return eventService.eventDetails(id);
    }

    @PostMapping("save")
    public void saveEvent(@Valid @RequestBody SaveEventDto event) {
        eventService.saveEvent(event);
    }

    @DeleteMapping("{id}/person/{personId}")
    public void removePersonFromEvent(@PathVariable Long id, @PathVariable Long personId) {
        eventService.removePersonFromEvent(id, personId);
    }

    @DeleteMapping("{id}/company/{companyId}")
    public void removeCompanyFromEvent(@PathVariable Long id, @PathVariable Long companyId) {
        eventService.removeCompanyFromEvent(id, companyId);
    }

    @DeleteMapping("{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
