package com.psp.PSPBackend.controller;

import com.psp.PSPBackend.dto.AuthRequest;
import com.psp.PSPBackend.dto.AuthResponse;
import com.psp.PSPBackend.dto.BuyRequest;
import com.psp.PSPBackend.webClient.PrimaryBankClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    @Autowired
    private PrimaryBankClient primaryBankClient;
    @PostMapping(path = "/buy")
    public ResponseEntity<AuthResponse> buy(@RequestBody BuyRequest buyRequest) {
        AuthResponse response = primaryBankClient.auth(new AuthRequest(buyRequest.getMerchantId(), "aa", buyRequest.getAmount(), 1, LocalDateTime.now()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
