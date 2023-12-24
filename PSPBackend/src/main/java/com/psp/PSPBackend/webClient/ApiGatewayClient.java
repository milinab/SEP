package com.psp.PSPBackend.webClient;

import com.psp.PSPBackend.dto.AuthRequest;
import com.psp.PSPBackend.dto.AuthResponse;
import com.psp.PSPBackend.dto.PaymentRequest;
import com.psp.PSPBackend.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange

public interface ApiGatewayClient {
    @PostExchange("/api/proxy/redirect")
    public AuthResponse redirectByPaymentType(@RequestBody AuthRequest authRequest);

    @PostExchange("/api/proxy/redirectPayment")
    public PaymentResponse redirectPayment(@RequestBody PaymentRequest paymentRequest);
}
