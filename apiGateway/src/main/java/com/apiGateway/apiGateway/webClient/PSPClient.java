package com.apiGateway.apiGateway.webClient;

import com.apiGateway.apiGateway.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface PSPClient {
    @PostExchange("/api/payment/buy")
    public ResponseEntity<AuthResponse> buy(@RequestBody BuyRequest buyRequest);

    @PostExchange("/api/payment/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest paymentRequest);

    @PostExchange("/api/payment/completePayment")
    public CompletedOrder completePayment(@RequestParam("token") String token);
}
