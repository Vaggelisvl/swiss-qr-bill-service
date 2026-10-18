package dev.vrailas.qrbill.domain;

import java.math.BigDecimal;

/** Test data builders shared across test packages. */
public final class QrBillTestFixtures {

    private QrBillTestFixtures() {
    }

    public static QrBill qrIbanBill() {
        return new QrBill(new Iban("CH4431999123000889012"),
                new Address("Robert Schneider AG", "Rue du Lac", "1268", "2501", "Biel", "CH"),
                new Amount(new BigDecimal("1949.75")), Currency.CHF,
                new Address("Pia-Maria Rutschmann-Schnyder", "Grosse Marktgasse", "28", "9400", "Rorschach", "CH"),
                new QrReference("210000000003139471430009017"), "Invoice 10978");
    }
}
