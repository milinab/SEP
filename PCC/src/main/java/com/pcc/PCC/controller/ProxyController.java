package com.pcc.PCC.controller;

import com.pcc.PCC.dto.PccRequest;
import com.pcc.PCC.dto.PccResponse;
import com.pcc.PCC.enums.PaymentStatus;
import com.pcc.PCC.model.Transaction;
import com.pcc.PCC.repository.TransactionRepository;
import com.pcc.PCC.webClient.PrimaryBankClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PostExchange;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class ProxyController {

    @Autowired
    private PrimaryBankClient primaryBankClient;

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${bank.code}")
    private String bankCode;
    @PostMapping("/sendToIssuerBank")
    public PccResponse sendToIssuerBank(@RequestBody PccRequest pccRequest) {
        try {
            if(pccRequest.getPan().substring(4, 8).equals(bankCode)) {
                Transaction transaction = new Transaction(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(),
                        null, null, null);
                transactionRepository.save(transaction);

                PccResponse response = primaryBankClient.issuerBankPayment(pccRequest);

                transaction.setIssuerOrderId(response.getIssuerOrderId());
                transaction.setIssuerTimestamp(response.getIssuerTimestamp());
                transaction.setPaymentStatus(response.getPaymentStatus());
                transactionRepository.save(transaction);

                return response;
            } else {
                return new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(), -1, null,
                        PaymentStatus.ERROR);
            }
        } catch (NullPointerException e) {
            return new PccResponse(pccRequest.getAcquiererOrderId(), pccRequest.getAcquiererTimestamp(), -1, null,
                    PaymentStatus.ERROR);
        }
    }
}
