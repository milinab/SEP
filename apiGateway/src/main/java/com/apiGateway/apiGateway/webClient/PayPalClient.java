package com.apiGateway.apiGateway.webClient;

import com.apiGateway.apiGateway.dto.*;
import com.google.zxing.WriterException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.io.IOException;

@HttpExchange
public interface PayPalClient {

    @PostExchange("/api/payment/init")
    public PaymentOrder createPayment(@RequestParam("sum") Double sum);

    @PostExchange("/api/payment/capture")
    public CompletedOrder completePayment(@RequestParam("token") String token);
}
