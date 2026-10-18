package dev.vrailas.qrbill.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReferenceTest {

    private static final String VALID_QRR = "210000000003139471430009017";

    @Test
    void acceptsValidQrReferenceAndIgnoresSpaces() {
        assertThat(new QrReference("21 00000 00003 13947 14300 09017").value()).isEqualTo(VALID_QRR);
        assertThat(new QrReference(VALID_QRR).type()).isEqualTo("QRR");
    }

    @Test
    void rejectsWrongCheckDigit() {
        assertThatThrownBy(() -> new QrReference("210000000003139471430009018"))
                .isInstanceOf(InvalidQrBillException.class).hasMessageContaining("check digit");
    }

    @Test
    void rejectsWrongLength() {
        assertThatThrownBy(() -> new QrReference("2100000000031394714300090"))
                .isInstanceOf(InvalidQrBillException.class);
    }

    @Test
    void acceptsValidCreditorReference() {
        CreditorReference reference = new CreditorReference("RF18 5390 0754 7034");
        assertThat(reference.value()).isEqualTo("RF18539007547034");
        assertThat(reference.type()).isEqualTo("SCOR");
    }

    @Test
    void rejectsCreditorReferenceWithBadChecksum() {
        assertThatThrownBy(() -> new CreditorReference("RF19539007547034"))
                .isInstanceOf(InvalidQrBillException.class);
    }

    @Test
    void noReferenceHasEmptyValue() {
        assertThat(new NoReference().type()).isEqualTo("NON");
        assertThat(new NoReference().value()).isEmpty();
    }
}
