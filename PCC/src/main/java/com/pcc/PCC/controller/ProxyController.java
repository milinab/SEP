package com.pcc.PCC.controller;

import com.pcc.PCC.dto.PccRequest;
import com.pcc.PCC.dto.PccResponse;
import com.pcc.PCC.enums.PaymentStatus;
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

    @Value("${bank.code}")
    private String bankCode;
    @PostMapping("/sendToIssuerBank")
    public PccResponse sendToIssuerBank(@RequestBody PccRequest pccRequest) {
        if(pccRequest.getPan().substring(4, 8).equals(bankCode)) {
            PccResponse response = primaryBankClient.issuerBankPayment(pccRequest);
            System.out.println("pcc response");
            System.out.println(response.getAcquirerOrderId());
            System.out.println(response.getPaymentStatus());
            return response;
        } else {
            return new PccResponse(-1, LocalDateTime.now(), -1, LocalDateTime.now(), PaymentStatus.FAILED);
        }
    }
}
