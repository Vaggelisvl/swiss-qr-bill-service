package dev.vrailas.qrbill.adapter.in.web;

import dev.vrailas.qrbill.domain.InvalidQrBillException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Maps domain rule violations to RFC 9457 problem details (422 Unprocessable Content). */
@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(InvalidQrBillException.class)
    ProblemDetail onInvalidQrBill(InvalidQrBillException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        problem.setTitle("Invalid QR-bill");
        return problem;
    }
}
