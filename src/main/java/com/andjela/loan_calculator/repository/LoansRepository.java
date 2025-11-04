package com.andjela.loan_calculator.repository;

import com.andjela.loan_calculator.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoansRepository extends JpaRepository<Loan, Long> {
}
