package com.pcc.PCC.repository;

import com.pcc.PCC.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    public Transaction findTransactionByAcquirerOrderId(Integer acquirerOrderId);
}
