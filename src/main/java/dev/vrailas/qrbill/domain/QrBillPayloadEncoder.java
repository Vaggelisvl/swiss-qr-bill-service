package dev.vrailas.qrbill.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Encodes a {@link QrBill} into the text payload of the Swiss QR code (SIX Swiss Implementation
 * Guidelines, version 2.0). Pure function: no I/O, no framework dependencies.
 */
public final class QrBillPayloadEncoder {

    static final String QR_TYPE = "SPC";
    static final String VERSION = "0200";
    static final String CODING_UTF8 = "1";
    static final String ADDRESS_TYPE_STRUCTURED = "S";
    static final String TRAILER = "EPD";
    static final String SEPARATOR = "\n";

    public String encode(QrBill bill) {
        List<String> lines = new ArrayList<>(32);
        lines.add(QR_TYPE);
        lines.add(VERSION);
        lines.add(CODING_UTF8);
        lines.add(bill.creditorIban().value());
        addAddress(lines, bill.creditor());
        addEmptyAddress(lines); // ultimate creditor: reserved for future use
        lines.add(bill.amountIfPresent().map(Amount::toPayloadString).orElse(""));
        lines.add(bill.currency().name());
        bill.debtorIfPresent().ifPresentOrElse(debtor -> addAddress(lines, debtor), () -> addEmptyAddress(lines));
        lines.add(bill.reference().type());
        lines.add(bill.reference().value());
        lines.add(bill.additionalInformation());
        lines.add(TRAILER);
        lines.add(""); // structured billing information: not supported
        return String.join(SEPARATOR, lines);
    }

    private static void addAddress(List<String> lines, Address address) {
        lines.add(ADDRESS_TYPE_STRUCTURED);
        lines.add(address.name());
        lines.add(address.street());
        lines.add(address.buildingNumber());
        lines.add(address.postalCode());
        lines.add(address.town());
        lines.add(address.country());
    }

    private static void addEmptyAddress(List<String> lines) {
        for (int i = 0; i < 7; i++) {
            lines.add("");
        }
    }
}
