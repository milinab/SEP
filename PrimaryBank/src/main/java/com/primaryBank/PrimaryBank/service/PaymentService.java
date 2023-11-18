package com.primaryBank.PrimaryBank.service;

import com.primaryBank.PrimaryBank.dto.AuthRequest;
import com.primaryBank.PrimaryBank.dto.PaymentRequest;
import com.primaryBank.PrimaryBank.dto.PccRequest;
import com.primaryBank.PrimaryBank.dto.PccResponse;
import com.primaryBank.PrimaryBank.model.Client;
import com.primaryBank.PrimaryBank.model.Transaction;
import com.primaryBank.PrimaryBank.repository.ClientRepository;
import com.primaryBank.PrimaryBank.repository.TransactionRepository;
import com.primaryBank.PrimaryBank.webClient.PccClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PccClient pccClient;

    @Value("${bank.code}")
    private String bankCode;

    public Integer clientExists(AuthRequest authRequest){
        Client client = clientRepository.findClientByMerchantId(authRequest.getMerchantId());
        if(client != null && client.getMerchantPassword().equals(authRequest.getMerchantPassword())){
            Transaction transaction = new Transaction(authRequest.getMerchantOrderId(), authRequest.getMerchantId(),
                    authRequest.getAmount(), authRequest.getMerchantTimeStamp());
            transaction = transactionRepository.save(transaction);
            return transaction.getPaymentId();
        }else {
            return null;
        }
    }

    public PccResponse checkIssuerBank(PaymentRequest paymentRequest) {
        if(paymentRequest.getPan().substring(4, 8).equals(bankCode)) {
            this.executePayment(); //implementirati skidanje sa racuna kupca i dodavanje na racun prodavca
        } else {
            PccResponse response = pccClient.sendToIssuerBank(new PccRequest(paymentRequest.getPan(),
                    paymentRequest.getExpDate(), paymentRequest.getCvv(), paymentRequest.getCardHolderName(),
                    paymentRequest.getPaymentId(), LocalDateTime.now()));
            return response;
        }
        return new PccResponse();// skontati sta vratiti
    }

    private void executePayment(){}
}
