package com.andjela.loan_calculator.integration;

import com.andjela.loan_calculator.entity.Installment;
import com.andjela.loan_calculator.entity.Loan;
import com.andjela.loan_calculator.repository.InstallmentsRepository;
import com.andjela.loan_calculator.repository.LoansRepository;
import com.andjela.loan_calculator.request.LoanRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private InstallmentsRepository installmentsRepository;

    @Test
    void shouldCreateLoanAndReturnInstallments() {
        LoanRequest request = new LoanRequest(
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(5),
                10
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoanRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<Installment[]> response = restTemplate.postForEntity("/api/loans/schedule", entity, Installment[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Installment[] installments = response.getBody();
        assertThat(installments).isNotEmpty();
        assertThat(installments).hasSize(10);

        // and persisted entities
        List<Loan> loans = loansRepository.findAll();
        assertThat(loans).hasSize(1);

        List<Installment> persistedInstallments = installmentsRepository.findAll();
        assertThat(persistedInstallments).hasSize(10);

        Loan loan = loans.get(0);
        assertThat(loan.getTotalInterest()).isEqualByComparingTo(BigDecimal.valueOf(23.10));
    }

    @Test
    void shouldReturnBadRequestForInvalidLoanRequest() {
        LoanRequest request = new LoanRequest(
                BigDecimal.valueOf(-1000),
                BigDecimal.ZERO,
                0
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoanRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<String> response = restTemplate
                .postForEntity("/api/loans/schedule", entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("must be positive");
    }
}
