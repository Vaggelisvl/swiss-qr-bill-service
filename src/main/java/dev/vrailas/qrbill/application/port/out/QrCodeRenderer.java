package dev.vrailas.qrbill.application.port.out;

/** Driven port: renders a QR-bill payload as a QR code image. */
public interface QrCodeRenderer {

    /** @return PNG bytes of the QR code including the Swiss cross */
    byte[] renderPng(String payload);
}
