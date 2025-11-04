package com.andjela.loan_calculator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NonNull
    BigDecimal loanAmount; // the amount of money you borrow
    @NonNull
    BigDecimal annualInterestRate; // the cost of borrowing money, paid in addition to your principal; the percentage of the principal that is used to calculate total interest, typically a yearly % rate.
    @NonNull
    Integer numberOfMonthlyPayments;
    BigDecimal totalPayments;
    BigDecimal totalInterest;
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Installment> installments = new ArrayList<>();

//    LoanStatus status;
//    @NonNull
//    Date startDate;
    //clientId;

    public Loan(@NonNull BigDecimal loanAmount, @NonNull BigDecimal annualInterestRate, @NonNull Integer numberOfMonthlyPayments, BigDecimal totalPayments, BigDecimal totalInterest) {
        this.loanAmount = loanAmount;
        this.annualInterestRate = annualInterestRate;
        this.numberOfMonthlyPayments = numberOfMonthlyPayments;
        this.totalPayments = totalPayments;
        this.totalInterest = totalInterest;
    }
}

