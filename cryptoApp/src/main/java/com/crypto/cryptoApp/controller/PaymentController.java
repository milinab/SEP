package com.crypto.cryptoApp.controller;

import com.crypto.cryptoApp.dto.AuthRequest;
import com.crypto.cryptoApp.dto.AuthResponse;
import com.crypto.cryptoApp.dto.CryptoPayingRequest;
import com.crypto.cryptoApp.dto.CryptoPayingResponse;
import com.crypto.cryptoApp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(path = "/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest) {
        return new AuthResponse(-1, "success crypto", 0.0);
    }

    @PostMapping("")
    public String pay(@RequestBody CryptoPayingRequest cryptoPayingRequest) {
        CryptoPayingResponse response = paymentService.pay(cryptoPayingRequest);
        return response.getPaymentUrl();
    }
}
