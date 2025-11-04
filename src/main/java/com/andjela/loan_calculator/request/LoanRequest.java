package com.andjela.loan_calculator.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class LoanRequest {
    @NotNull
    @DecimalMin(value = "0.01", message = "Loan amount must be positive")
    BigDecimal loanAmount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Annual interest rate must be positive")
    BigDecimal annualInterestRate;

    @NotNull
    @Min(value = 1, message = "Number of monthly payments must be at least 1")
    Integer numberOfMonthlyPayments;
}
