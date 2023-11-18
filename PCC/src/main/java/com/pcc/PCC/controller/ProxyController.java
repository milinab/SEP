package com.pcc.PCC.controller;

import com.pcc.PCC.dto.PccRequest;
import com.pcc.PCC.dto.PccResponse;
import com.pcc.PCC.webClient.PrimaryBankClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PostExchange;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class ProxyController {

    @Autowired
    private PrimaryBankClient primaryBankClient;
    @PostExchange("/sendToIssuerBank")
    public PccResponse sendToIssuerBank(@RequestBody PccRequest pccRequest) {
        //return new PccResponse("success");
        //napraviti proveru koji je broj kartice i onda kao na osnovu toga gadjati odredjenu banku kupca
        return primaryBankClient.issuerBankPayment(pccRequest);
    }
}
