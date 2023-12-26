package com.apiGateway.apiGateway.webClient;

import com.apiGateway.apiGateway.dto.CryptoPayingRequest;
import com.apiGateway.apiGateway.dto.PccRequest;
import com.apiGateway.apiGateway.dto.PccResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface CryptoPaymentClient {

    @PostExchange("/api/payment")
    public String pay(@RequestBody CryptoPayingRequest cryptoPayingRequest);

}
