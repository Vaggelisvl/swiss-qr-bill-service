package dev.vrailas.qrbill.adapter.out.qr;

/** Unexpected failure while rendering a QR code (an infrastructure problem, not a user error). */
public final class QrCodeRenderingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QrCodeRenderingException(String message, Throwable cause) {
        super(message, cause);
    }
}
