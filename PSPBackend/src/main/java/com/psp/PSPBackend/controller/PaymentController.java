package com.psp.PSPBackend.controller;

import com.psp.PSPBackend.dto.AuthRequest;
import com.psp.PSPBackend.dto.AuthResponse;
import com.psp.PSPBackend.dto.BuyRequest;
import com.psp.PSPBackend.dto.TransactionStartsDTO;
import com.psp.PSPBackend.enums.PaymentStatus;
import com.psp.PSPBackend.enums.PaymentType;
import com.psp.PSPBackend.model.Client;
import com.psp.PSPBackend.model.Transaction;
import com.psp.PSPBackend.repository.TransactionRepository;
import com.psp.PSPBackend.service.ClientService;
import com.psp.PSPBackend.webClient.CryptoClient;
import com.psp.PSPBackend.webClient.PayPalClient;
import com.psp.PSPBackend.webClient.PrimaryBankClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class PaymentController {

    @Autowired
    private PrimaryBankClient primaryBankClient;

    @Autowired
    private PayPalClient payPalClient;
    @Autowired
    private CryptoClient cryptoClient;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionRepository transactionRepository;
    @PostMapping(path = "/buy")
    public ResponseEntity<AuthResponse> buy(@RequestBody BuyRequest buyRequest) {

        try {
            if(buyRequest.getPaymentType().equals(PaymentType.CREDIT_CARD)) {
                // izgenerisati merchant order id
                Client client = clientService.findClientByMerchantId(buyRequest.getMerchantId());
                if(client != null) {
                    LocalDateTime merchantTimeStamp = LocalDateTime.now();
                    AuthResponse response = primaryBankClient.auth(new AuthRequest(buyRequest.getMerchantId(), client.getMerchantPassword(),
                            buyRequest.getAmount(), buyRequest.getMerchantOrderId(), merchantTimeStamp));
                    if(response != null) {
                        Transaction transaction = new Transaction(buyRequest.getMerchantOrderId(), buyRequest.getMerchantId(),
                                buyRequest.getAmount(), merchantTimeStamp, null); //scheduler da bi se promenilo
                        transactionRepository.save(transaction);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new AuthResponse(-1, "failed", buyRequest.getAmount()),
                                HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>(new AuthResponse(-1, "failed", buyRequest.getAmount()),
                            HttpStatus.BAD_REQUEST);
                }
            } else if (buyRequest.getPaymentType().equals(PaymentType.PAYPAL)) {
                AuthResponse response = payPalClient.auth(new AuthRequest());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (buyRequest.getPaymentType().equals(PaymentType.CRYPTO)) {
                AuthResponse response = cryptoClient.auth(new AuthRequest());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                AuthResponse response = primaryBankClient.QRPay(new AuthRequest());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (NullPointerException e){
            return new ResponseEntity<>(new AuthResponse(-1, "error", buyRequest.getAmount()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/calculateAmount")
    public ResponseEntity<String> calculateAmount(@RequestBody TransactionStartsDTO transactionStartsDTO) {
        if (transactionStartsDTO != null) {

            String pspFE = "http://localhost:4200";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", pspFE);

            return new ResponseEntity<>(headers, HttpStatus.FOUND);
            //return ResponseEntity.ok(agencyAmount);

        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
