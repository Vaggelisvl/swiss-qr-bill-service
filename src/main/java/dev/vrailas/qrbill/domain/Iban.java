package dev.vrailas.qrbill.domain;

import java.util.regex.Pattern;

/**
 * Swiss or Liechtenstein IBAN. A <em>QR-IBAN</em> is an IBAN whose institution identifier lies in
 * 30000-31999; it requires a QR reference ({@link QrReference}).
 */
public record Iban(String value) {

    private static final Pattern FORMAT = Pattern.compile("(CH|LI)\\d{2}[0-9A-Z]{17}");
    private static final int QR_IID_MIN = 30_000;
    private static final int QR_IID_MAX = 31_999;

    public Iban {
        String normalized = value == null ? "" : value.replaceAll("\\s+", "").toUpperCase();
        Checks.require(FORMAT.matcher(normalized).matches(), "IBAN must be a Swiss or Liechtenstein IBAN with 21 characters");
        Checks.require(Iso7064.hasValidChecksum(normalized), "IBAN checksum is invalid");
        value = normalized;
    }

    public boolean isQrIban() {
        String institutionId = value.substring(4, 9);
        if (!institutionId.chars().allMatch(Character::isDigit)) {
            return false;
        }
        int id = Integer.parseInt(institutionId);
        return id >= QR_IID_MIN && id <= QR_IID_MAX;
    }

    /** IBAN grouped in blocks of four characters, e.g. {@code CH93 0076 2011 6238 5295 7}. */
    public String formatted() {
        return value.replaceAll("(.{4})(?=.)", "$1 ");
    }
}
