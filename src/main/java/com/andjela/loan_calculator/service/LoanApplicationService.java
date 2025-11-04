package com.andjela.loan_calculator.service;

import com.andjela.loan_calculator.dto.InstallmentDto;
import com.andjela.loan_calculator.entity.Loan;
import com.andjela.loan_calculator.mapper.InstallmentMapper;
import com.andjela.loan_calculator.repository.LoansRepository;
import com.andjela.loan_calculator.request.LoanRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoanApplicationService {
    private final LoanCalculationService loanCalculationService;
    private final LoansRepository loansRepository;
    private final InstallmentMapper installmentMapper;

    @Transactional
    public List<InstallmentDto> createLoanAndGetInstallments(LoanRequest loanRequest) {
        final Loan loan = createLoanWithInstallments(loanRequest);

        return loan.getInstallments().stream()
                .map(installmentMapper::toDto)
                .collect(Collectors.toList());
    }

    private Loan createLoanWithInstallments(LoanRequest loanRequest) {
        Loan loan = loanCalculationService.calculateLoanWithInstallments(loanRequest);
        loansRepository.save(loan);
        return loan;
    }
}
