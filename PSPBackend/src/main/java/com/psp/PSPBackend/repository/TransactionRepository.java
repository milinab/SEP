package com.psp.PSPBackend.repository;

import com.psp.PSPBackend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public Transaction findTransactionByMerchantOrderId(Long merchantOrderId);
}
