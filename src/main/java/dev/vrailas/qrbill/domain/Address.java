package dev.vrailas.qrbill.domain;

import java.util.regex.Pattern;

/**
 * Structured postal address (address type "S" of the QR-bill specification).
 *
 * <p>Street and building number are optional; everything else is mandatory.
 */
public record Address(String name, String street, String buildingNumber, String postalCode, String town, String country) {

    private static final Pattern COUNTRY = Pattern.compile("[A-Z]{2}");

    public Address {
        name = Checks.requireText(name, "name", 70);
        street = Checks.optionalText(street, "street", 70);
        buildingNumber = Checks.optionalText(buildingNumber, "buildingNumber", 16);
        postalCode = Checks.requireText(postalCode, "postalCode", 16);
        town = Checks.requireText(town, "town", 35);
        country = country == null ? "" : country.strip().toUpperCase();
        Checks.require(COUNTRY.matcher(country).matches(), "country must be a two-letter ISO 3166 code");
    }
}
