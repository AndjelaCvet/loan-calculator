package com.andjela.loan_calculator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/*
One monthly loan payment
 */
@Entity
@Getter
@Setter
@Table(name = "installments")
@NoArgsConstructor
public class Installment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Integer installmentNumber;
    @NotNull
    private Integer periodInMonths; //(add later.. enum for PaymentFrequency month or year)
    @NotNull
    private BigDecimal paymentAmount;
    @NotNull
    private BigDecimal principalAmount;
    @NotNull
    private BigDecimal interestAmount;
    @NotNull
    private BigDecimal balanceOwed;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    public Installment(Integer installmentNumber, Integer periodInMonths, BigDecimal paymentAmount, BigDecimal principalAmount, BigDecimal InterestAmount, BigDecimal balanceOwed, Loan loan) {
        this.installmentNumber = installmentNumber;
        this.periodInMonths = periodInMonths;
        this.paymentAmount = paymentAmount;
        this.principalAmount = principalAmount;
        this.interestAmount = InterestAmount;
        this.balanceOwed = balanceOwed;
        this.loan = loan;
    }

}
