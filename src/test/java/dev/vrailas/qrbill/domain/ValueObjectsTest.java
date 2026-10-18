package dev.vrailas.qrbill.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueObjectsTest {

    @Test
    void amountIsRenderedWithTwoDecimals() {
        assertThat(new Amount(new BigDecimal("1949.7")).toPayloadString()).isEqualTo("1949.70");
        assertThat(new Amount(new BigDecimal("5")).toPayloadString()).isEqualTo("5.00");
    }

    @Test
    void amountRejectsOutOfRangeAndTooManyDecimals() {
        assertThatThrownBy(() -> new Amount(new BigDecimal("0"))).isInstanceOf(InvalidQrBillException.class);
        assertThatThrownBy(() -> new Amount(new BigDecimal("1000000000.00"))).isInstanceOf(InvalidQrBillException.class);
        assertThatThrownBy(() -> new Amount(new BigDecimal("1.234"))).isInstanceOf(InvalidQrBillException.class);
        assertThatThrownBy(() -> new Amount(null)).isInstanceOf(InvalidQrBillException.class);
    }

    @Test
    void addressNormalizesOptionalFieldsAndCountry() {
        Address address = new Address(" Robert Schneider AG ", null, null, "2501", "Biel", "ch");
        assertThat(address.name()).isEqualTo("Robert Schneider AG");
        assertThat(address.street()).isEmpty();
        assertThat(address.country()).isEqualTo("CH");
    }

    @Test
    void addressRejectsMissingMandatoryFieldsAndBadCountry() {
        assertThatThrownBy(() -> new Address("", null, null, "2501", "Biel", "CH")).isInstanceOf(InvalidQrBillException.class);
        assertThatThrownBy(() -> new Address("A", null, null, "", "Biel", "CH")).isInstanceOf(InvalidQrBillException.class);
        assertThatThrownBy(() -> new Address("A", null, null, "2501", "Biel", "CHE")).isInstanceOf(InvalidQrBillException.class);
        assertThatThrownBy(() -> new Address("x".repeat(71), null, null, "2501", "Biel", "CH")).isInstanceOf(InvalidQrBillException.class);
    }
}
