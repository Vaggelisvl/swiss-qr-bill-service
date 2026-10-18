package dev.vrailas.qrbill.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IbanTest {

    @Test
    void normalizesSpacesAndCase() {
        assertThat(new Iban("ch93 0076 2011 6238 5295 7").value()).isEqualTo("CH9300762011623852957");
    }

    @Test
    void regularIbanIsNotQrIban() {
        assertThat(new Iban("CH9300762011623852957").isQrIban()).isFalse();
    }

    @Test
    void recognizesQrIban() {
        assertThat(new Iban("CH4431999123000889012").isQrIban()).isTrue();
    }

    @Test
    void acceptsLiechtensteinIban() {
        assertThat(new Iban("LI21088100002324013AA").value()).isEqualTo("LI21088100002324013AA");
    }

    @Test
    void formatsInGroupsOfFour() {
        assertThat(new Iban("CH9300762011623852957").formatted()).isEqualTo("CH93 0076 2011 6238 5295 7");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "CH9300762011623852958", "DE89370400440532013000", "CH93007620116238529", "not an iban"})
    void rejectsInvalidValues(String value) {
        assertThatThrownBy(() -> new Iban(value)).isInstanceOf(InvalidQrBillException.class);
    }

    @Test
    void rejectsNull() {
        assertThatThrownBy(() -> new Iban(null)).isInstanceOf(InvalidQrBillException.class);
    }
}
