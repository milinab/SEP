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

    public Client updatePaymentMethods(String merchantId, String password, Boolean creditCardEnabled, Boolean qrCodeEnabled, Boolean paypalEnabled, Boolean cryptoEnabled) {
        //Client client = clientRepository.findById(merchantId)
                //.orElseThrow(() -> new RuntimeException("Client not found"));
        Client client = clientRepository.findClientByMerchantId(merchantId);
        if(client == null) {
            Client newClient = new Client(merchantId, password, creditCardEnabled, qrCodeEnabled, paypalEnabled, cryptoEnabled);
            return clientRepository.save(newClient);
        }
        client.setCreditCardEnabled(creditCardEnabled);
        client.setQrCodeEnabled(qrCodeEnabled);
        client.setPaypalEnabled(paypalEnabled);
        client.setCryptoEnabled(cryptoEnabled);


        return clientRepository.save(client);
    }

    public Client getClient(String merchantId) {
        Client client = clientRepository.findClientByMerchantId(merchantId);
        return client;
    }
}
