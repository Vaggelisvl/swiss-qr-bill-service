package dev.vrailas.qrbill.adapter.in.web;

import dev.vrailas.qrbill.application.port.in.GenerateQrBillUseCase;
import dev.vrailas.qrbill.application.port.in.GenerateQrBillUseCase.GeneratedQrBill;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api/v1/qr-bills")
class QrBillController {

    private final GenerateQrBillUseCase useCase;

    QrBillController(GenerateQrBillUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    QrBillResponse generate(@Valid @RequestBody QrBillRequest request) {
        GeneratedQrBill result = useCase.generate(QrBillMapper.toDomain(request));
        return new QrBillResponse(result.payload(), Base64.getEncoder().encodeToString(result.qrCodePng()));
    }

    @PostMapping(path = "/image", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
    byte[] generateImage(@Valid @RequestBody QrBillRequest request) {
        return useCase.generate(QrBillMapper.toDomain(request)).qrCodePng();
    }
}
