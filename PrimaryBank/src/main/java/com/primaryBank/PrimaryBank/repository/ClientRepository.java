package com.primaryBank.PrimaryBank.repository;

import com.primaryBank.PrimaryBank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, String> {
    //@Query("select c from clients c where c.merchant_id = ?1")
    Client findClientByMerchantId(String merchantId);
}
