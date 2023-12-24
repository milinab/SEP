package com.apiGateway.apiGateway.webClient;

import com.apiGateway.apiGateway.dto.AuthRequest;
import com.apiGateway.apiGateway.dto.AuthResponse;
import com.apiGateway.apiGateway.dto.PaymentRequest;
import com.apiGateway.apiGateway.dto.PaymentResponse;
import com.apiGateway.apiGateway.dto.PccRequest;
import com.apiGateway.apiGateway.dto.PccResponse;
import com.google.zxing.WriterException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.io.IOException;

@HttpExchange
public interface PayPalClient {

    @PostExchange("/api/payment/init")
    public PaymentOrder createPayment(@RequestParam("sum") Double sum);
}
