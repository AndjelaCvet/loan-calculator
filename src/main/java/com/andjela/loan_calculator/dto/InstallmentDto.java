package com.andjela.loan_calculator.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class InstallmentDto {
    Long id;
    Integer installmentNumber;
    Integer periodInMonths;
    BigDecimal paymentAmount;
    BigDecimal principalAmount;
    BigDecimal interestAmount;
    BigDecimal balanceOwed;
    Long loanId;
}
