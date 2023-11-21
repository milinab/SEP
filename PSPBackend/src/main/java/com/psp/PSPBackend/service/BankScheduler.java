package com.psp.PSPBackend.service;

import com.psp.PSPBackend.dto.TransactionDto;
import com.psp.PSPBackend.webClient.PrimaryBankClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankScheduler {

    @Autowired
    private PrimaryBankClient primaryBankClient;
    @Scheduled(fixedRate = 3600000) // 1 sat = 3600000 milisekundi
    public void schedulePrimaryBankTask() {
        // Poziva servis svaki sat
        List<TransactionDto> transactions = primaryBankClient.getTransactions();
        for (TransactionDto transaction: transactions) {
            // dobavi transakcioju po merchantOrderId i izmenim mu payment status
        }
    }
}
