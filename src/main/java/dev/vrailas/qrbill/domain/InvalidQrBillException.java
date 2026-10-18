package dev.vrailas.qrbill.domain;

/** Thrown when a value or combination of values violates the Swiss QR-bill rules. */
public final class InvalidQrBillException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidQrBillException(String message) {
        super(message);
    }
}
