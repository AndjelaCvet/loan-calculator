package com.andjela.loan_calculator.unit;


import com.andjela.loan_calculator.entity.Installment;
import com.andjela.loan_calculator.entity.Loan;
import com.andjela.loan_calculator.request.LoanRequest;
import com.andjela.loan_calculator.service.LoanCalculationService;
import com.andjela.loan_calculator.service.LoanCalculationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoanCalculationServiceTest {

    private LoanCalculationService loanCalculationService;

    @BeforeEach
    void setUp() {
        loanCalculationService = new LoanCalculationServiceImpl();
    }

    @Test
    void shouldCalculateTotalLoanWithInstallments() {
        LoanRequest request = new LoanRequest(
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(5),
                10
        );

        // when
        Loan loan = loanCalculationService.calculateLoanWithInstallments(request);

        assertThat(loan).isNotNull();
        List<Installment> installments = loan.getInstallments();
        assertThat(installments).hasSize(10);

        BigDecimal totalPayments = installments.stream()
                .map(Installment::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(loan.getTotalPayments()).isEqualByComparingTo(totalPayments);

        BigDecimal totalInterest = totalPayments.subtract(loan.getLoanAmount());
        assertThat(loan.getTotalInterest()).isEqualByComparingTo(totalInterest);

        // check first installment values
        Installment first = installments.get(0);
        assertThat(first.getInstallmentNumber()).isEqualTo(1);
        assertThat(first.getPaymentAmount()).isNotNull();
        assertThat(first.getPrincipalAmount()).isNotNull();
        assertThat(first.getInterestAmount()).isNotNull();
        assertThat(first.getBalanceOwed()).isNotNull();
    }

    @Test
    void shouldCalculateLoanWithInstallmentsCorrectly() {
        LoanRequest request = new LoanRequest(
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(5),
                10
        );

        // when
        Loan loan = loanCalculationService.calculateLoanWithInstallments(request);
        List<Installment> installments = loan.getInstallments();


        assertThat(installments).hasSize(10);

        // expected values for each installment
        BigDecimal[][] expected = new BigDecimal[][] {
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(98.14), BigDecimal.valueOf(4.17), BigDecimal.valueOf(901.86)},
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(98.55), BigDecimal.valueOf(3.76), BigDecimal.valueOf(803.31)},
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(98.96), BigDecimal.valueOf(3.35), BigDecimal.valueOf(704.35)},
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(99.38), BigDecimal.valueOf(2.93), BigDecimal.valueOf(604.97)},
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(99.79), BigDecimal.valueOf(2.52), BigDecimal.valueOf(505.18)},
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(100.21), BigDecimal.valueOf(2.10), BigDecimal.valueOf(404.97)},
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(100.62), BigDecimal.valueOf(1.69), BigDecimal.valueOf(304.35)},
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(101.04), BigDecimal.valueOf(1.27), BigDecimal.valueOf(203.31)},
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(101.46), BigDecimal.valueOf(0.85), BigDecimal.valueOf(101.85)},
                {BigDecimal.valueOf(102.31), BigDecimal.valueOf(101.89), BigDecimal.valueOf(0.42), BigDecimal.valueOf(0.00)}
        };

        for (int i = 0; i < installments.size(); i++) {
            Installment ins = installments.get(i);
            assertThat(ins.getPaymentAmount()).isEqualByComparingTo(expected[i][0]);
            assertThat(ins.getPrincipalAmount()).isEqualByComparingTo(expected[i][1]);
            assertThat(ins.getInterestAmount()).isEqualByComparingTo(expected[i][2]);
            assertThat(ins.getBalanceOwed()).isEqualByComparingTo(expected[i][3]);
        }

        // optional: check totals
        BigDecimal expectedTotalPayments = BigDecimal.valueOf(1023.10);
        BigDecimal expectedTotalInterest = BigDecimal.valueOf(23.10);

        assertThat(loan.getTotalPayments()).isEqualByComparingTo(expectedTotalPayments);
        assertThat(loan.getTotalInterest()).isEqualByComparingTo(expectedTotalInterest);
    }


    @Test
    void shouldThrowExceptionForNegativedLoanAmountRequest() {
        LoanRequest invalidRequest = new LoanRequest(
                BigDecimal.valueOf(-1000),
                BigDecimal.valueOf(0),
                0
        );
        try {
            loanCalculationService.calculateLoanWithInstallments(invalidRequest);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("Loan amount must be positive");
        }
    }

    @Test
    void shouldThrowExceptionForInvalidLoanRequest() {
        LoanRequest invalidRequest = new LoanRequest(
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(0.5),
                0
        );
        try {
            loanCalculationService.calculateLoanWithInstallments(invalidRequest);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("Number of monthly payments must be at least 1");
        }
    }

    @Test
    void shouldThrowExceptionForNegativeLoanInterestRateRequest() {
        LoanRequest invalidRequest = new LoanRequest(
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(-1),
                0
        );
        try {
            loanCalculationService.calculateLoanWithInstallments(invalidRequest);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("Annual interest rate cannot be negative");
        }
    }
}
