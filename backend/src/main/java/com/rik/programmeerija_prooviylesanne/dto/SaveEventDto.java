package com.rik.programmeerija_prooviylesanne.dto;

import com.rik.programmeerija_prooviylesanne.controller.validation.FutureTimestamp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
public record SaveEventDto(
    @NotBlank(message = "Nimi on kohustuslik")
    String name,
    @NotNull(message = "Toimumisaeg on kohustuslik")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @FutureTimestamp
    LocalDateTime timestamp,
    @NotBlank(message = "Koht on kohustuslik")
    String place,
    @Size(max = 1000, message = "Maksimaalne pikkus on {max} sümbolid")
    String info
) {}
