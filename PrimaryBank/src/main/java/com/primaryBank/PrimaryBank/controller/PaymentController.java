package com.primaryBank.PrimaryBank.controller;

import com.google.zxing.WriterException;
import com.primaryBank.PrimaryBank.dto.*;
import com.primaryBank.PrimaryBank.enums.PaymentStatus;
import com.primaryBank.PrimaryBank.model.Transaction;
import com.primaryBank.PrimaryBank.service.PaymentService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @PostMapping(path = "/auth")
    public AuthResponse auth(@RequestBody AuthRequest authRequest) {
        Integer paymentId = paymentService.clientExists(authRequest);
        if(paymentId != null){
            AuthResponse response = new AuthResponse(paymentId, "success", authRequest.getAmount(), null);
            return response;
        }else {
            return null;
        }
    }

    @PostMapping(path = "/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.checkIssuerBank(paymentRequest);
    }

    @PostMapping("/issuerPayment")
    public PccResponse issuerBankPayment(@RequestBody PccRequest pccRequest){
        return paymentService.issuerBankPayment(pccRequest);
    }

    /*@PostMapping(path = "/QRPay")
    public AuthResponse QRPay(@RequestBody AuthRequest authRequest) {
        return new AuthResponse(-1,
                "success QR", 0.0);
    }*/

    @GetMapping(path = "/getTransactions")
    public List<TransactionDto> getTransactions() {
        List<TransactionDto> ret = new ArrayList<>();
        for (Transaction t: paymentService.getAll()) {
            ret.add(new TransactionDto(t.getPaymentId(), t.getMerchantOrderId(), t.getMerchantId(), t.getAmount(),
                    t.getMerchantTimeStamp(), t.getPaymentStatus(), t.getAcquiererTimestamp(), t.getIssuerTimestamp()));
        }
        return ret;
    }

    @PostMapping(path = "/generateQRcode")
    public AuthResponse generateQRcode(@RequestBody AuthRequest authRequest) throws IOException, WriterException {
        return paymentService.generateQRcode(authRequest);
    }

    @PostMapping(path = "/payQRcode")
    public PaymentResponse payQRcode(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.payQrCode(paymentRequest);
    }

    @PostMapping("/issuerPaymentQRcode")
    public PccResponse issuerBankPaymentQRcode(@RequestBody PccRequest pccRequest){
        return paymentService.issuerBankPaymentQRcode(pccRequest);
    }

}
