package com.andjela.loan_calculator.mapper;

import com.andjela.loan_calculator.dto.InstallmentDto;
import com.andjela.loan_calculator.entity.Installment;
import org.springframework.stereotype.Component;

@Component
public class InstallmentMapper {

    public InstallmentDto toDto(Installment installment) {
        if (installment == null) {
            throw new IllegalArgumentException("Installment cannot be null");
        }
        return new InstallmentDto(
                installment.getId(),
                installment.getInstallmentNumber(),
                installment.getPeriodInMonths(),
                installment.getPaymentAmount(),
                installment.getPrincipalAmount(),
                installment.getInterestAmount(),
                installment.getBalanceOwed(),
                installment.getLoan() != null ? installment.getLoan().getId() : null
        );
    }
}
