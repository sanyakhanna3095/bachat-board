package com.cg.budgetboard.repository;

import com.cg.budgetboard.model.Income;
import com.cg.budgetboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);

    @Query("SELECT i FROM Income i WHERE i.user.id = :userId AND FUNCTION('DATE_FORMAT', i.date, '%m-%Y') = :month")
    List<Income> findByUserAndMonth(@Param("userId") Long userId, @Param("month") String month);
}
