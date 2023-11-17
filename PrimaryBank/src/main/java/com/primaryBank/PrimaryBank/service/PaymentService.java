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

    public boolean clientExists(AuthRequest authRequest){
        Client client = clientRepository.findClientByMerchantId(authRequest.getMerchantId());
        if(client != null && client.getMerchantPassword().equals(authRequest.getMerchantPassword())){
            Transaction transaction = new Transaction(authRequest.getMerchantOrderId(), authRequest.getMerchantId(),
                    authRequest.getAmount(), authRequest.getMerchantTimeStamp(), 0); // Kako generisati ovaj id???
            transactionRepository.save(transaction);
            return true;
        }else {
            return false;
        }
    }

    public PccResponse checkIssuerBank(PaymentRequest paymentRequest) {
        if(paymentRequest.getPan().substring(4, 8).equals(bankCode)) {
            this.executePayment(); //implementirati skidanje sa racuna kupca i dodavanje na racun prodavca
        } else {
            PccResponse response = pccClient.sendToIssuerBank(new PccRequest(paymentRequest.getPaymentId(), paymentRequest.getPan(),
                    paymentRequest.getExpDate(), paymentRequest.getCvv(), paymentRequest.getCardHolderName(),
                    123, LocalDateTime.now()));
            return response;
        }
        return new PccResponse("error");// skontati sta vratiti
    }

    private void executePayment(){}
}
