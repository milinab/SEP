package com.psp.PSPBackend.webClient;

import com.psp.PSPBackend.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange

public interface ApiGatewayClient {
    @PostExchange("/api/proxy/redirect")
    public AuthResponse redirectByPaymentType(@RequestBody AuthRequest authRequest) throws Exception;

    @PostExchange("/api/proxy/redirectPayment")
    public PaymentResponse redirectPayment(@RequestBody PaymentRequest paymentRequest);

    @PostExchange("/api/proxy/redirectPayPalPayment")
    public CompletedOrder redirectPayPalPayment(@RequestParam("token") String token);
}
