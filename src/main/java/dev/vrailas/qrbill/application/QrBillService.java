package dev.vrailas.qrbill.application;

import dev.vrailas.qrbill.application.port.in.GenerateQrBillUseCase;
import dev.vrailas.qrbill.application.port.out.QrCodeRenderer;
import dev.vrailas.qrbill.domain.QrBill;
import dev.vrailas.qrbill.domain.QrBillPayloadEncoder;

import java.util.Objects;

/** Application service orchestrating the domain encoder and the rendering port. Framework-free. */
public final class QrBillService implements GenerateQrBillUseCase {

    private final QrBillPayloadEncoder encoder;
    private final QrCodeRenderer renderer;

    public QrBillService(QrBillPayloadEncoder encoder, QrCodeRenderer renderer) {
        this.encoder = Objects.requireNonNull(encoder);
        this.renderer = Objects.requireNonNull(renderer);
    }

    @Override
    public GeneratedQrBill generate(QrBill bill) {
        String payload = encoder.encode(Objects.requireNonNull(bill, "bill"));
        return new GeneratedQrBill(payload, renderer.renderPng(payload));
    }
}
