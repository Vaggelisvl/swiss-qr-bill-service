package dev.vrailas.qrbill.domain;

/** Payment reference of a QR-bill. Sealed so that every consumer handles all variants exhaustively. */
public sealed interface PaymentReference permits QrReference, CreditorReference, NoReference {

    /** Reference type code as written in the payload: QRR, SCOR or NON. */
    String type();

    /** Reference value as written in the payload; empty for {@link NoReference}. */
    String value();
}
