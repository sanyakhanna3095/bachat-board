package com.cg.budgetboard.repository;

import com.cg.budgetboard.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId AND e.category.id = :categoryId")
    Double getTotalExpenseByUserAndCategory(@Param("userId") Long userId, @Param("categoryId") Long categoryId);

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND FUNCTION('DATE_FORMAT', e.date, '%m-%Y') = :month")
    List<Expense> findByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND e.date BETWEEN :startDate AND :endDate")
    List<Expense> findByUserIdAndDateRange(@Param("userId") Long userId,
                                           @Param("startDate") LocalDate start,
                                           @Param("endDate") LocalDate end);
}