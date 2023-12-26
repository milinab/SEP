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

    public Client updatePaymentMethods(String merchantId, Boolean creditCardEnabled, Boolean qrCodeEnabled, Boolean paypalEnabled, Boolean cryptoEnabled) {
        Client client = clientRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        client.setCreditCardEnabled(creditCardEnabled);
        client.setQrCodeEnabled(qrCodeEnabled);
        client.setPaypalEnabled(paypalEnabled);
        client.setCryptoEnabled(cryptoEnabled);


        return clientRepository.save(client);
    }
}
