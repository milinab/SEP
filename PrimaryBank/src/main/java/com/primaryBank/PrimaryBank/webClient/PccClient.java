package com.primaryBank.PrimaryBank.webClient;

import com.primaryBank.PrimaryBank.dto.AuthRequest;
import com.primaryBank.PrimaryBank.dto.AuthResponse;
import com.primaryBank.PrimaryBank.dto.PaymentRequest;
import com.primaryBank.PrimaryBank.dto.PccRequest;
import com.primaryBank.PrimaryBank.dto.PccResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface PccClient {

    @PostExchange("/api/payment/sendToIssuerBank")
    public PccResponse sendToIssuerBank(@RequestBody PccRequest pccRequest);
}
