package com.rik.programmeerija_prooviylesanne.dto;

public record EventParticipantDto(
    Long id,
    String fullName,
    String code,
    ParticipantType participantType
) {}