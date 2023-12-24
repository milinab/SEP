package com.psp.PSPBackend.service;

import com.psp.PSPBackend.dto.TransactionDto;
import com.psp.PSPBackend.model.Transaction;
import com.psp.PSPBackend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankScheduler {
    @Autowired
    private TransactionRepository transactionRepository;
    @Scheduled(fixedRate = 120000) // 1 sat = 3600000 milisekundi
    public void schedulePrimaryBankTask() {

        /*List<TransactionDto> transactions = primaryBankClient.getTransactions();
        for (TransactionDto transaction: transactions) {
            Transaction update = transactionRepository.findTransactionByMerchantOrderId(transaction.getMerchantOrderId());
            if(update != null){
                update.setPaymentStatus(transaction.getPaymentStatus());
                transactionRepository.save(update);
            }
        }*/
    }
}
