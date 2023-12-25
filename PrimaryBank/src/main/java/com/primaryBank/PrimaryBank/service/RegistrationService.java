package com.primaryBank.PrimaryBank.service;

import com.primaryBank.PrimaryBank.dto.ClientDto;
import com.primaryBank.PrimaryBank.model.Client;
import com.primaryBank.PrimaryBank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class RegistrationService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private KeyStoreService keyStoreService;

    public Client addClient(ClientDto dto) throws Exception {

        SecretKey key = generateSecretKey();

        Client client = new Client(dto.getMerchantId(), encrypt(dto.getMerchantPassword(),dto.getMerchantId(), dto.getPan(), key),
                0, dto.getPan(), encrypt(dto.getExpDate(), dto.getMerchantId(), dto.getPan(), key), encrypt(dto.getCvv(), dto.getMerchantId(), dto.getPan(), key),
                encrypt(dto.getCardHolderName(), dto.getMerchantId(), dto.getPan(), key), dto.getAccountNumber(), encrypt(dto.getName(), dto.getMerchantId(), dto.getPan(), key)); // da li enkriptovati available sum

        return clientRepository.save(client);
    }

    public SecretKey generateSecretKey() throws Exception{
        return keyStoreService.generateKey();
    }

    public String encrypt(String value, String merchantId, String pan, SecretKey key) throws Exception {
        String encriptedValue = keyStoreService.encrypt(value, key);
        keyStoreService.addKey(merchantId, pan, key);

        return encriptedValue;
    }
}
