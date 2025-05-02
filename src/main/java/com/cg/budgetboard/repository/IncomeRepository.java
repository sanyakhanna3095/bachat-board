package com.cg.budgetboard.repository;

import com.cg.budgetboard.model.Income;
import com.cg.budgetboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);

    List<Income> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
