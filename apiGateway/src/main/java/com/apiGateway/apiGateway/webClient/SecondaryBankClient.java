package com.apiGateway.apiGateway.webClient;

import com.apiGateway.apiGateway.dto.PccRequest;
import com.apiGateway.apiGateway.dto.PccResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface SecondaryBankClient {

    @PostExchange("/api/payment/issuerPayment")
    public PccResponse issuerBankPayment(@RequestBody PccRequest pccRequest);
}
