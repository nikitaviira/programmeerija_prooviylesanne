package com.rik.programmeerija_prooviylesanne.dto;

import java.util.List;

public record EventDetailsDto(
    Long id,
    String name,
    String datetime,
    String place,
    List<EventParticipantDto> participants
) {}
