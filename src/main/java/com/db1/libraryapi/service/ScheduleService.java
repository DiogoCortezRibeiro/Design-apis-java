package com.db1.libraryapi.service;

import com.db1.libraryapi.domain.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

    private final EmailService emailService;
    private final LoanService loanService;

    @Scheduled(cron = CRON_LATE_LOANS)
    public void sendEmailToLateLoans() {
        List<Loan> loans = loanService.getAllLateLoans();
        List<String> emails = loans.stream().map(loan -> loan.getEmail()).collect(Collectors.toList());

        String message = "Atenção! você tem um emprestimo atrasado. Favor devolver o livro o mais rapido possível";

        emailService.sendEmails(emails, message);
    }
}
