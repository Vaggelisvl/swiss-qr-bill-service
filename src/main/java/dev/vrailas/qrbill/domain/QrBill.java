package dev.vrailas.qrbill.domain;

import java.util.Optional;

/**
 * Aggregate describing one Swiss QR-bill.
 *
 * <p>{@code amount} and {@code debtor} are optional (null when left open for the payer to fill in);
 * use {@link #amountIfPresent()} and {@link #debtorIfPresent()} to read them.
 * {@code additionalInformation} is an optional free-text message of at most 140 characters.
 *
 * <p>Invariants: a QR-IBAN requires a {@link QrReference}; a regular IBAN must not carry one.
 */
public record QrBill(
        Iban creditorIban,
        Address creditor,
        Amount amount,
        Currency currency,
        Address debtor,
        PaymentReference reference,
        String additionalInformation) {

    private static final int MAX_ADDITIONAL_INFORMATION = 140;

    public QrBill {
        Checks.require(creditorIban != null, "creditorIban must not be null");
        Checks.require(creditor != null, "creditor must not be null");
        Checks.require(currency != null, "currency must not be null");
        reference = reference == null ? new NoReference() : reference;
        additionalInformation = Checks.optionalText(additionalInformation, "additionalInformation", MAX_ADDITIONAL_INFORMATION);

        boolean qrReference = reference instanceof QrReference;
        Checks.require(creditorIban.isQrIban() == qrReference,
                creditorIban.isQrIban()
                        ? "a QR-IBAN requires a QR reference (QRR)"
                        : "a QR reference (QRR) requires a QR-IBAN");
    }

    public Optional<Amount> amountIfPresent() {
        return Optional.ofNullable(amount);
    }

    public Optional<Address> debtorIfPresent() {
        return Optional.ofNullable(debtor);
    }
}
