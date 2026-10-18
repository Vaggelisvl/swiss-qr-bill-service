# ADR 0001: Hexagonal architecture with an executable dependency rule

**Status:** accepted

## Context
The valuable part of this service is the QR-bill rule set (IBAN, references, payload layout). Web framework and QR rendering library are replaceable details.

## Decision
- `domain`: value objects and the payload encoder. Pure Java, no framework imports.
- `application`: use case and ports (`GenerateQrBillUseCase`, `QrCodeRenderer`). Framework-free.
- `adapter.in.web` / `adapter.out.qr`: Spring MVC and ZXing, behind the ports.
- `config`: the only place where Spring wires the core to the adapters.

The dependency rule is enforced by ArchUnit tests (`ArchitectureTest`), so a violation fails the build.

## Consequences
The core is tested without Spring and runs in milliseconds. Swapping ZXing or adding a CLI or messaging adapter does not touch the domain. Cost: a few more types (ports, mappers).
