# ADR 0002: Self-validating immutable value objects and sealed types

**Status:** accepted

## Decision
- Every domain concept (`Iban`, `Amount`, `Address`, `QrReference`, `CreditorReference`) is an immutable `record` that validates and normalises in its compact constructor. An instance that exists is valid.
- `PaymentReference` is a `sealed interface`; consumers use exhaustive `switch` patterns, so adding a variant is a compile error until every use is handled.
- Cross-field rules (QR-IBAN requires a QR reference) live in the `QrBill` aggregate.
- A single `InvalidQrBillException` is mapped to HTTP 422 problem details (RFC 9457) at the edge only.

## Consequences
No validation scattered through services, no "validate before use" calls to forget. Bean Validation on the DTOs only checks transport shape (presence), never business rules.
