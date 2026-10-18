package dev.vrailas.qrbill.domain;

/** Small guard helpers shared by the value objects. */
final class Checks {

    private Checks() {
    }

    static String requireText(String value, String field, int maxLength) {
        String trimmed = value == null ? "" : value.strip();
        if (trimmed.isEmpty()) {
            throw new InvalidQrBillException(field + " must not be blank");
        }
        return limit(trimmed, field, maxLength);
    }

    static String optionalText(String value, String field, int maxLength) {
        String trimmed = value == null ? "" : value.strip();
        return limit(trimmed, field, maxLength);
    }

    static void require(boolean condition, String message) {
        if (!condition) {
            throw new InvalidQrBillException(message);
        }
    }

    private static String limit(String value, String field, int maxLength) {
        if (value.length() > maxLength) {
            throw new InvalidQrBillException(field + " must not exceed " + maxLength + " characters");
        }
        return value;
    }
}
