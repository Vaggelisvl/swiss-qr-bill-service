package dev.vrailas.qrbill.adapter.in.web;

/** JSON response: the payload text and the QR code as Base64 encoded PNG. */
public record QrBillResponse(String payload, String qrCodePngBase64) {
}
