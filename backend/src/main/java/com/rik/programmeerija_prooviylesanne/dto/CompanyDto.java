package com.rik.programmeerija_prooviylesanne.dto;

import com.rik.programmeerija_prooviylesanne.controller.validation.ValidCompanyRegistryCode;
import com.rik.programmeerija_prooviylesanne.model.PaymentType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record CompanyDto(
    @Nullable
    Long id,
    @NotBlank(message = "Nimi on kohustuslik")
    String name,
    @NotBlank(message = "Registrikood on kohustuslik")
    @ValidCompanyRegistryCode
    String registryCode,
    @NotNull(message = "Osavõtjate arv on kohustuslik")
    @Max(value = 100, message = "Maksimaalne osavõtjate arv on {value}")
    Integer participantsCount,
    @NotNull(message = "Maksmise viis on kohustuslik")
    PaymentType paymentType,
    @Size(max = 5000, message = "Maksimaalne pikkus on {max} sümbolid")
    String info
) {}
