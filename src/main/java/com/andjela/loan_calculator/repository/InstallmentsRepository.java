package com.andjela.loan_calculator.repository;

import com.andjela.loan_calculator.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentsRepository extends JpaRepository<Installment, Long> {
}
