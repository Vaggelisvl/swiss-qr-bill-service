package dev.vrailas.qrbill.domain;

/** Explicit absence of a reference ("NON"). Only allowed with a regular (non QR) IBAN. */
public record NoReference() implements PaymentReference {

    @Override
    public String type() {
        return "NON";
    }

    @Override
    public String value() {
        return "";
    }
}
