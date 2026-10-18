package dev.vrailas.qrbill.adapter.in.web;

import dev.vrailas.qrbill.domain.Address;
import dev.vrailas.qrbill.domain.Amount;
import dev.vrailas.qrbill.domain.CreditorReference;
import dev.vrailas.qrbill.domain.Currency;
import dev.vrailas.qrbill.domain.Iban;
import dev.vrailas.qrbill.domain.InvalidQrBillException;
import dev.vrailas.qrbill.domain.NoReference;
import dev.vrailas.qrbill.domain.PaymentReference;
import dev.vrailas.qrbill.domain.QrBill;
import dev.vrailas.qrbill.domain.QrReference;

import java.util.Locale;

/** Translates the transport model into domain objects; all rule violations surface as {@link InvalidQrBillException}. */
final class QrBillMapper {

    private QrBillMapper() {
    }

    static QrBill toDomain(QrBillRequest request) {
        return new QrBill(
                new Iban(request.creditorIban()),
                toDomain(request.creditor()),
                request.amount() == null ? null : new Amount(request.amount()),
                parseCurrency(request.currency()),
                request.debtor() == null ? null : toDomain(request.debtor()),
                toDomain(request.reference()),
                request.additionalInformation());
    }

    private static Address toDomain(QrBillRequest.AddressDto dto) {
        return new Address(dto.name(), dto.street(), dto.buildingNumber(), dto.postalCode(), dto.town(), dto.country());
    }

    private static Currency parseCurrency(String value) {
        try {
            return Currency.valueOf(value.strip().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new InvalidQrBillException("currency must be CHF or EUR");
        }
    }

    private static PaymentReference toDomain(QrBillRequest.ReferenceDto dto) {
        if (dto == null) {
            return new NoReference();
        }
        return switch (dto.type().strip().toUpperCase(Locale.ROOT)) {
            case "QRR" -> new QrReference(dto.value());
            case "SCOR" -> new CreditorReference(dto.value());
            case "NON" -> new NoReference();
            default -> throw new InvalidQrBillException("reference type must be QRR, SCOR or NON");
        };
    }
}
