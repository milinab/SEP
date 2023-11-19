package com.psp.PSPBackend.controller;

import com.psp.PSPBackend.dto.AuthRequest;
import com.psp.PSPBackend.dto.AuthResponse;
import com.psp.PSPBackend.dto.BuyRequest;
import com.psp.PSPBackend.model.Client;
import com.psp.PSPBackend.service.ClientService;
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

    @Autowired
    private ClientService clientService;
    @PostMapping(path = "/buy")
    public ResponseEntity<AuthResponse> buy(@RequestBody BuyRequest buyRequest) {
        // izgenerisati merchant order id
        Client client = clientService.findClientByMerchantId(buyRequest.getMerchantId());
        if(client != null) {
            AuthResponse response = primaryBankClient.auth(new AuthRequest(buyRequest.getMerchantId(), "aa",
                    buyRequest.getAmount(), buyRequest.getMerchantOrderId(), LocalDateTime.now()));
            if(response != null) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new AuthResponse(-1, "failed", buyRequest.getAmount()),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new AuthResponse(-1, "failed", buyRequest.getAmount()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
