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
public interface PrimaryBankClient {
    @PostExchange("/api/payment/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest) throws Exception;

    @PostExchange("/api/payment/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest paymentRequest);
    @PostExchange("/api/payment/generateQRcode")
    public AuthResponse generateQRcode(@RequestBody AuthRequest authRequest) throws Exception;

    @PostExchange("/api/payment/payQRcode")
    public PaymentResponse payQRcode(@RequestBody PaymentRequest paymentRequest);

}
