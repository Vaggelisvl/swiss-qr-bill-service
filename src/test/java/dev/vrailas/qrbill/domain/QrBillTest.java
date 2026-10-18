package dev.vrailas.qrbill.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QrBillTest {

    static final Address CREDITOR = new Address("Robert Schneider AG", "Rue du Lac", "1268", "2501", "Biel", "CH");
    static final Address DEBTOR = new Address("Pia-Maria Rutschmann-Schnyder", "Grosse Marktgasse", "28", "9400", "Rorschach", "CH");

    @Test
    void qrIbanRequiresQrReference() {
        assertThatThrownBy(() -> new QrBill(new Iban("CH4431999123000889012"), CREDITOR, null, Currency.CHF, null, null, null))
                .isInstanceOf(InvalidQrBillException.class).hasMessageContaining("requires a QR reference");
    }

    @Test
    void regularIbanRejectsQrReference() {
        assertThatThrownBy(() -> new QrBill(new Iban("CH9300762011623852957"), CREDITOR, null, Currency.CHF, null,
                new QrReference("210000000003139471430009017"), null))
                .isInstanceOf(InvalidQrBillException.class).hasMessageContaining("requires a QR-IBAN");
    }

    @Test
    void defaultsToNoReferenceAndEmptyInformation() {
        QrBill bill = new QrBill(new Iban("CH9300762011623852957"), CREDITOR, null, Currency.EUR, null, null, null);
        assertThat(bill.reference()).isInstanceOf(NoReference.class);
        assertThat(bill.additionalInformation()).isEmpty();
        assertThat(bill.amountIfPresent()).isEmpty();
        assertThat(bill.debtorIfPresent()).isEmpty();
    }

    @Test
    void rejectsTooLongAdditionalInformation() {
        assertThatThrownBy(() -> new QrBill(new Iban("CH9300762011623852957"), CREDITOR, null, Currency.CHF, null, null, "x".repeat(141)))
                .isInstanceOf(InvalidQrBillException.class);
    }

    @Test
    void requiresMandatoryParts() {
        assertThatThrownBy(() -> new QrBill(null, CREDITOR, null, Currency.CHF, null, null, null)).isInstanceOf(InvalidQrBillException.class);
        assertThatThrownBy(() -> new QrBill(new Iban("CH9300762011623852957"), null, null, Currency.CHF, null, null, null)).isInstanceOf(InvalidQrBillException.class);
        assertThatThrownBy(() -> new QrBill(new Iban("CH9300762011623852957"), CREDITOR, null, null, null, null, null)).isInstanceOf(InvalidQrBillException.class);
    }
}
