package com.apiGateway.apiGateway.controller;

import com.apiGateway.apiGateway.dto.*;
import com.apiGateway.apiGateway.enums.PaymentType;
import com.apiGateway.apiGateway.webClient.*;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/proxy")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class ProxyController {

    @Autowired
    private PSPClient pspClient;
    @Autowired
    private PrimaryBankClient primaryBankClient;

    @Autowired
    private PccClient pccClient;

    @Autowired
    private SecondaryBankClient secondaryBankClient;

    @Autowired
    private CryptoPaymentClient cryptoPaymentClient;
    @PostMapping(path = "/buy")
    public ResponseEntity<AuthResponse> buy(@RequestBody BuyRequest buyRequest) {
        return pspClient.buy(buyRequest);
    }

    //ovo je redirect kada se radi auth (pre placanja)
    @PostMapping(path = "/redirect")
    public AuthResponse redirectByPaymentType(@RequestBody AuthRequest authRequest) throws Exception {
        if(authRequest.getPaymentType().equals(PaymentType.CREDIT_CARD)) {
            return primaryBankClient.auth(authRequest);
        } else if (authRequest.getPaymentType().equals(PaymentType.QR_CODE)) {
            return primaryBankClient.generateQRcode(authRequest);
        } else if (authRequest.getPaymentType().equals(PaymentType.PAYPAL)) {
            return null;
        } else if (authRequest.getPaymentType().equals(PaymentType.CRYPTO)){
            CryptoPayingRequest cryptoRequest = new CryptoPayingRequest();
            cryptoRequest.setAmount(authRequest.getAmount());
            AuthResponse response;

            String paymentUrl = cryptoPaymentClient.pay(cryptoRequest);
            response = new AuthResponse(-1, paymentUrl, authRequest.getAmount(), null);
            return response;
        } else {
            return null;
        }
    }

    @PostMapping(path = "/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest paymentRequest) {
        return pspClient.pay(paymentRequest);
    }

    @PostMapping(path = "/redirectPayment")
    public PaymentResponse redirectPayment(@RequestBody PaymentRequest paymentRequest) {
        if(paymentRequest.getPaymentType().equals(PaymentType.CREDIT_CARD)) {
            return primaryBankClient.pay(paymentRequest);
        } else if (paymentRequest.getPaymentType().equals(PaymentType.QR_CODE)) {
            return primaryBankClient.payQRcode(paymentRequest);
        } else if (paymentRequest.getPaymentType().equals(PaymentType.PAYPAL)) {
            return null;
        } else {
            return null;
        }
    }

    @PostMapping(path = "/redirecToPcc")
    public PccResponse redirecToPcc(@RequestBody PccRequest pccRequest) {
        return pccClient.sendToIssuerBank(pccRequest);
    }

    @PostMapping(path = "/redirecToSecondaryBank")
    public PccResponse redirecToSecondaryBank(@RequestBody PccRequest pccRequest) {
        return secondaryBankClient.issuerBankPayment(pccRequest);
    }

    @PostMapping(path = "/redirecToPccQRcode")
    public PccResponse redirecToPccQRcode(@RequestBody PccRequest pccRequest) {
        return pccClient.sendToIssuerBankQRcode(pccRequest);
    }

    @PostMapping(path = "/redirecToSecondaryBankQRcode")
    public PccResponse redirecToSecondaryBankQRcode(@RequestBody PccRequest pccRequest) {
        return secondaryBankClient.issuerBankPaymentQRcode(pccRequest);
    }
}
