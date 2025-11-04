package com.andjela.loan_calculator.service;

import com.andjela.loan_calculator.entity.Loan;
import com.andjela.loan_calculator.request.LoanRequest;
import org.springframework.stereotype.Service;

@Service
public interface LoanCalculationService {

    Loan calculateLoanWithInstallments(LoanRequest loanRequest);
}
