package com.cg.budgetboard.controller;

import com.cg.budgetboard.services.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<String> getMonthlySummary(@RequestParam String month, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        reportService.sendMonthlySummaryReport(month, token);
        return ResponseEntity.ok("Monthly summary report sent.");
    }
}