package com.rik.programmeerija_prooviylesanne.controller;

import com.rik.programmeerija_prooviylesanne.dto.EventDto;
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

    @GetMapping("/future")
    public List<EventDto> futureEvents() {
        return eventService.futureEvents();
    }

    @GetMapping("/past")
    public List<EventDto> pastEvents() {
        return eventService.pastEvents();
    }

    @PostMapping("/save")
    public void saveEvent(@Valid @RequestBody SaveEventDto event) {
        eventService.saveEvent(event);
    }

    @DeleteMapping("{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
