package dev.vrailas.qrbill.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Monetary amount of a QR-bill: 0.01 to 999 999 999.99 with at most two decimals. */
public record Amount(BigDecimal value) {

    private static final BigDecimal MIN = new BigDecimal("0.01");
    private static final BigDecimal MAX = new BigDecimal("999999999.99");

    public Amount {
        Checks.require(value != null, "amount must not be null");
        Checks.require(value.stripTrailingZeros().scale() <= 2, "amount must not have more than two decimals");
        value = value.setScale(2, RoundingMode.UNNECESSARY);
        Checks.require(value.compareTo(MIN) >= 0 && value.compareTo(MAX) <= 0,
                "amount must be between 0.01 and 999999999.99");
    }

    /** Plain decimal representation with a dot and exactly two decimals, as required by the payload. */
    public String toPayloadString() {
        return value.toPlainString();
    }
}
