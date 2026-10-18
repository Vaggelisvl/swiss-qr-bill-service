package dev.vrailas.qrbill.adapter.in.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/** Transport model of the REST API. Business validation lives in the domain, not here. */
public record QrBillRequest(
        @NotBlank String creditorIban,
        @NotNull @Valid AddressDto creditor,
        BigDecimal amount,
        @NotBlank String currency,
        @Valid AddressDto debtor,
        @Valid ReferenceDto reference,
        String additionalInformation) {

    public record AddressDto(
            @NotBlank String name,
            String street,
            String buildingNumber,
            @NotBlank String postalCode,
            @NotBlank String town,
            @NotBlank String country) {
    }

    /** {@code type} is one of QRR, SCOR or NON; {@code value} is ignored for NON. */
    public record ReferenceDto(@NotBlank String type, String value) {
    }
}
