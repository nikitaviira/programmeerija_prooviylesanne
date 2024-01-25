package com.rik.programmeerija_prooviylesanne.dto;

import com.rik.programmeerija_prooviylesanne.controller.validation.ValidCompanyRegistryCode;
import com.rik.programmeerija_prooviylesanne.model.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

@Validated
public record CompanyDto(
    @NotBlank(message = "Nimi on kohustuslik")
    String name,
    @NotBlank(message = "Registrikood on kohustuslik")
    @ValidCompanyRegistryCode
    String registryCode,
    @NotNull(message = "Osavõtjate arv on kohustuslik")
    @Range(max = 100, min = 0, message = "Osavõtjate arv peab olema {min} ja {max} vahel")
    Integer participantsCount,
    @NotNull(message = "Maksmise viis on kohustuslik")
    PaymentType paymentType,
    @Size(max = 5000, message = "Maksimaalne lisainfo pikkus on {max} sümbolid")
    String info
) {}
