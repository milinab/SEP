package com.apiGateway.apiGateway.webClient;

import com.apiGateway.apiGateway.dto.AuthRequest;
import com.apiGateway.apiGateway.dto.AuthResponse;
import com.apiGateway.apiGateway.dto.PaymentRequest;
import com.apiGateway.apiGateway.dto.PaymentResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface PrimaryBankClient {
    @PostExchange("/api/payment/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest);

    @PostExchange("/api/payment/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest paymentRequest);
}
