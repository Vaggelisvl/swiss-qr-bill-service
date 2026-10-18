package dev.vrailas.qrbill.config;

import dev.vrailas.qrbill.adapter.out.qr.ZxingQrCodeRenderer;
import dev.vrailas.qrbill.application.QrBillService;
import dev.vrailas.qrbill.application.port.in.GenerateQrBillUseCase;
import dev.vrailas.qrbill.application.port.out.QrCodeRenderer;
import dev.vrailas.qrbill.domain.QrBillPayloadEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Composition root: the only place that wires the framework-free core to its adapters. */
@Configuration(proxyBeanMethods = false)
class BeanConfig {

    @Bean
    QrBillPayloadEncoder qrBillPayloadEncoder() {
        return new QrBillPayloadEncoder();
    }

    @Bean
    QrCodeRenderer qrCodeRenderer() {
        return new ZxingQrCodeRenderer();
    }

    @Bean
    GenerateQrBillUseCase generateQrBillUseCase(QrBillPayloadEncoder encoder, QrCodeRenderer renderer) {
        return new QrBillService(encoder, renderer);
    }
}
