package com.psp.PSPBackend.service;

import com.psp.PSPBackend.model.Client;
import com.psp.PSPBackend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client findClientByMerchantId(String merchantId){
        return clientRepository.findClientByMerchantId(merchantId);
    }
}
