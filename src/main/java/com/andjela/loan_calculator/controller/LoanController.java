package com.andjela.loan_calculator.controller;

import com.andjela.loan_calculator.dto.InstallmentDto;
import com.andjela.loan_calculator.request.LoanRequest;
import com.andjela.loan_calculator.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(LoanController.BASE_PATH)
public class LoanController {
    static final String BASE_PATH = "/api/loans";

    private static final String INSTALLMENTS_VERSION_JSON = "application/com.andjela.loan_calculator-api.installments-v1+json";

    private final LoanApplicationService loanApplicationService;

    // POST api/loans
//    private static final String LOAN_VERSION_JSON = "application/com.andjela.loan_calculator-api.loan-v1+json";
    // GET api/loans/{id}/installments

    @PostMapping(
            value = "/schedule",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = INSTALLMENTS_VERSION_JSON
    )
    public ResponseEntity<List<InstallmentDto>> createLoanAndReturnSchedule(@RequestBody @Valid LoanRequest loanRequest) {
        List<InstallmentDto> installments = loanApplicationService.createLoanAndGetInstallments(loanRequest);
        return ResponseEntity.ok(installments);
    }
}
