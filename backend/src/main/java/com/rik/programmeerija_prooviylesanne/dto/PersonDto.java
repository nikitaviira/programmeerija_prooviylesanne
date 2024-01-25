package com.rik.programmeerija_prooviylesanne.dto;

import com.rik.programmeerija_prooviylesanne.controller.validation.ValidPersonalCode;
import com.rik.programmeerija_prooviylesanne.model.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record PersonDto(
    @NotBlank(message = "Eesnimi on kohustuslik")
    String firstName,
    @NotBlank(message = "Perenimi on kohustuslik")
    String lastName,
    @NotBlank(message = "Isikukood on kohustuslik")
    @ValidPersonalCode
    String personalCode,
    @NotNull(message = "Maksmise viis on kohustuslik")
    PaymentType paymentType,
    @Size(max = 1500, message = "Maksimaalne lisainfo pikkus on {max} sümbolid")
    String info
) {}
