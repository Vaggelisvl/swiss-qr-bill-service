# ADR 0003: Scope of the implemented specification

**Status:** accepted

Implements the Swiss Payment Standards QR-bill payload (version 2.0 layout, 32 lines) with:
- structured addresses (type `S`) for creditor and debtor,
- reference types QRR (mod 10 recursive), SCOR (ISO 11649, mod 97) and NON,
- QR-IBAN detection (institution identifier 30000-31999),
- PNG QR code at 46 mm / 300 dpi with the Swiss cross.

Out of scope for now: combined addresses (`K`), ultimate creditor, structured billing information, alternative procedures, and the full payment part layout (PDF/SVG). The output is **not** certified by SIX; validate with the official SIX validation portal before production use.
