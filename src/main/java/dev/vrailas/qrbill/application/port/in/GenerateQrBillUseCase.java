package dev.vrailas.qrbill.application.port.in;

import dev.vrailas.qrbill.domain.QrBill;

/** Driving port: turns a validated {@link QrBill} into its payload and QR code image. */
public interface GenerateQrBillUseCase {

    GeneratedQrBill generate(QrBill bill);

    /** Result of the use case. {@code qrCodePng} is a defensive copy on access. */
    record GeneratedQrBill(String payload, byte[] qrCodePng) {

        public GeneratedQrBill {
            qrCodePng = qrCodePng.clone();
        }

        @Override
        public byte[] qrCodePng() {
            return qrCodePng.clone();
        }
    }
}
