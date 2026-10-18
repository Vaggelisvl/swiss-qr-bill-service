package dev.vrailas.qrbill.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class QrBillPayloadEncoderTest {

    private final QrBillPayloadEncoder encoder = new QrBillPayloadEncoder();

    @Test
    void encodesAllFieldsInSpecificationOrder() {
        String[] lines = encoder.encode(QrBillTestFixtures.qrIbanBill()).split("\n", -1);

        assertThat(lines).hasSize(32);
        assertThat(lines).startsWith("SPC", "0200", "1", "CH4431999123000889012", "S", "Robert Schneider AG", "Rue du Lac", "1268", "2501", "Biel", "CH");
        assertThat(lines[11]).isEmpty(); // ultimate creditor address type
        assertThat(lines[18]).isEqualTo("1949.75");
        assertThat(lines[19]).isEqualTo("CHF");
        assertThat(lines[20]).isEqualTo("S");
        assertThat(lines[21]).isEqualTo("Pia-Maria Rutschmann-Schnyder");
        assertThat(lines[27]).isEqualTo("QRR");
        assertThat(lines[28]).isEqualTo("210000000003139471430009017");
        assertThat(lines[29]).isEqualTo("Invoice 10978");
        assertThat(lines[30]).isEqualTo("EPD");
    }

    @Test
    void leavesOptionalPartsEmpty() {
        QrBill bill = new QrBill(new Iban("CH9300762011623852957"), QrBillTest.CREDITOR, null, Currency.EUR, null, null, null);
        String[] lines = encoder.encode(bill).split("\n", -1);

        assertThat(lines).hasSize(32);
        assertThat(lines[18]).isEmpty();
        assertThat(lines[19]).isEqualTo("EUR");
        assertThat(lines[20]).isEmpty();
        assertThat(lines[27]).isEqualTo("NON");
        assertThat(lines[28]).isEmpty();
    }

    @Test
    void encodesCreditorReference() {
        QrBill bill = new QrBill(new Iban("CH9300762011623852957"), QrBillTest.CREDITOR, new Amount(BigDecimal.TEN), Currency.CHF,
                null, new CreditorReference("RF18539007547034"), null);
        String[] lines = encoder.encode(bill).split("\n", -1);

        assertThat(lines[27]).isEqualTo("SCOR");
        assertThat(lines[28]).isEqualTo("RF18539007547034");
        assertThat(lines[18]).isEqualTo("10.00");
    }
}
