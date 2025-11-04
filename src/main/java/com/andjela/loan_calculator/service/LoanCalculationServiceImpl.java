package com.andjela.loan_calculator.service;

import com.andjela.loan_calculator.entity.Installment;
import com.andjela.loan_calculator.entity.Loan;
import com.andjela.loan_calculator.request.LoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanCalculationServiceImpl implements LoanCalculationService {

    @Override
    public Loan calculateLoanWithInstallments(LoanRequest request) {
        validateLoanInputs(request.getLoanAmount(), request.getAnnualInterestRate(), request.getNumberOfMonthlyPayments());
        Loan loan = new Loan(request.getLoanAmount(), request.getAnnualInterestRate(), request.getNumberOfMonthlyPayments(), null, null);
        List<Installment> installments = calculateInstallments(loan);
        loan.setInstallments(installments);

        BigDecimal totalPayments = installments.stream()
                .map(Installment::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalInterest = totalPayments.subtract(loan.getLoanAmount());

        loan.setTotalPayments(totalPayments);
        loan.setTotalInterest(totalInterest);

        return loan;
    }

    private List<Installment> calculateInstallments(Loan loan) {
        BigDecimal monthlyPayment = calculateMonthlyPayment(
                loan.getLoanAmount(),
                loan.getAnnualInterestRate(),
                loan.getNumberOfMonthlyPayments()
        );

        List<Installment> installments = new ArrayList<>();
        BigDecimal balance = loan.getLoanAmount();
        BigDecimal monthlyRate = loan.getAnnualInterestRate().divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        for (int month = 1; month <= loan.getNumberOfMonthlyPayments(); month++) {
            BigDecimal interest = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principal = monthlyPayment.subtract(interest).setScale(2, RoundingMode.HALF_UP);

            balance = balance.subtract(principal).setScale(2, RoundingMode.HALF_UP);
            if (balance.compareTo(BigDecimal.ZERO) < 0) balance = BigDecimal.ZERO;
//  //          to fix last mounth
//            if (month == loan.getNumberOfMonthlyPayments()) {
//                principal = balance;
//                monthlyPayment = principal.add(interest).setScale(2, RoundingMode.HALF_UP);
//                balance = BigDecimal.ZERO;
//            } else {
//                balance = balance.subtract(principal).setScale(2, RoundingMode.HALF_UP);
//            }
            installments.add(new Installment(month, loan.getNumberOfMonthlyPayments(),
                    monthlyPayment.setScale(2, RoundingMode.HALF_DOWN), principal, interest, balance, loan));
        }

        return installments;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal annualInterestRate, int numberOfMonthlyPayments) {
        BigDecimal monthlyRate = annualInterestRate
                .divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        // (1 + i)^n
        BigDecimal onePlusRatePowerN = BigDecimal.ONE.add(monthlyRate).pow(numberOfMonthlyPayments);
        // Numerator: P * i * (1 + i)^n
        BigDecimal numerator = loanAmount.multiply(monthlyRate).multiply(onePlusRatePowerN);
        // Denominator: (1 + i)^n - 1
        BigDecimal denominator = onePlusRatePowerN.subtract(BigDecimal.ONE);
        // Monthly payment = numerator / denominator
        BigDecimal monthlyPayment = numerator.divide(denominator, 10, RoundingMode.HALF_UP);

        return monthlyPayment;
    }

    private void validateLoanInputs(BigDecimal loanAmount, BigDecimal annualInterestRate, int numberOfMonthlyPayments) {
        if (loanAmount == null || loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if (annualInterestRate == null || annualInterestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Annual interest rate cannot be negative");
        }
        if (numberOfMonthlyPayments < 1) {
            throw new IllegalArgumentException("Number of monthly payments must be at least 1");
        }
    }
}
