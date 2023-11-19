package com.psp.PSPBackend.repository;

import com.psp.PSPBackend.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
    Client findClientByMerchantId(String merchantId);
}
