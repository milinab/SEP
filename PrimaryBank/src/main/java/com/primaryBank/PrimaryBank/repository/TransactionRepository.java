package com.primaryBank.PrimaryBank.repository;

import com.primaryBank.PrimaryBank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findTransactionByPaymentId(Integer paymentId);
}
