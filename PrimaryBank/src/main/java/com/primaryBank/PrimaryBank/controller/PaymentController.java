package com.primaryBank.PrimaryBank.controller;

import com.primaryBank.PrimaryBank.dto.AuthRequest;
import com.primaryBank.PrimaryBank.dto.AuthResponse;
import com.primaryBank.PrimaryBank.dto.PaymentRequest;
import com.primaryBank.PrimaryBank.dto.PccRequest;
import com.primaryBank.PrimaryBank.dto.PccResponse;
import com.primaryBank.PrimaryBank.service.PaymentService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PostExchange;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @PostMapping(path = "/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest) {
        if(paymentService.clientExists(authRequest)){
            AuthResponse response = new AuthResponse(0, "succes", authRequest.getAmount());
            return response;
        }else {
            return null;
        }
    }

    @PostMapping(path = "/pay")
    public PccResponse pay(@RequestBody PaymentRequest paymentRequest) {
        PccResponse response = paymentService.checkIssuerBank(paymentRequest);
        return response;
    } // izmeniti ovaj response

    @PostMapping("/issuerPayment")
    public PccResponse issuerBankPayment(@RequestBody PccRequest authRequest){
        return new PccResponse("success");
    }
}
