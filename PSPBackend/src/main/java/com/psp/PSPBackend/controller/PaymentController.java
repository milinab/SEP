package com.psp.PSPBackend.controller;

import com.psp.PSPBackend.dto.*;
import com.psp.PSPBackend.enums.PaymentStatus;
import com.psp.PSPBackend.enums.PaymentType;
import com.psp.PSPBackend.model.Client;
import com.psp.PSPBackend.model.Transaction;
import com.psp.PSPBackend.repository.TransactionRepository;
import com.psp.PSPBackend.service.ClientService;
import com.psp.PSPBackend.webClient.ApiGatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class PaymentController {


    @Autowired
    private ClientService clientService;
    @Autowired
    private ApiGatewayClient apiGatewayClient;

    @Autowired
    private TransactionRepository transactionRepository;
    @PostMapping(path = "/buy")
    public ResponseEntity<AuthResponse> buy(@RequestBody BuyRequest buyRequest) {

        try {
            Client client = clientService.findClientByMerchantId(buyRequest.getMerchantId());
            if(client != null) {
                LocalDateTime merchantTimeStamp = LocalDateTime.now();
                AuthResponse response = apiGatewayClient.redirectByPaymentType(new AuthRequest(buyRequest.getMerchantId(),
                        client.getMerchantPassword(), buyRequest.getAmount(), buyRequest.getMerchantOrderId(), merchantTimeStamp,
                        buyRequest.getPaymentType()));
                if(response != null) {
                    Transaction transaction = new Transaction(-1, response.getPaymentId(),buyRequest.getMerchantOrderId(), buyRequest.getMerchantId(),
                            buyRequest.getAmount(), merchantTimeStamp, null); //scheduler da bi se promenilo
                    transactionRepository.save(transaction);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new AuthResponse(-1, "failed", buyRequest.getAmount(), null),
                            HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new AuthResponse(-1, "failed", buyRequest.getAmount(), null),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            return new ResponseEntity<>(new AuthResponse(-1, "error", buyRequest.getAmount(), null),
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

    @PostMapping(path = "/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = apiGatewayClient.redirectPayment(paymentRequest);
        Transaction merchantTransaction = transactionRepository.findTransactionByMerchantOrderId(response.getMerchantOrderId());
        merchantTransaction.setPaymentStatus(response.getPaymentStatus());
        transactionRepository.save(merchantTransaction);

        if(response.getIssuerOrderId() != null){
            Transaction issuerTransaction = new Transaction(-1, response.getIssuerOrderId(), response.getMerchantOrderId(),
                     paymentRequest.getPan(), merchantTransaction.getAmount(), merchantTransaction.getMerchantTimeStamp(),
                    response.getPaymentStatus());
            transactionRepository.save(issuerTransaction);
        }
        return response;
    }


    @PutMapping("/{clientId}/payment-methods")
    public ResponseEntity<Client> updatePaymentMethods(
            @PathVariable String merchantId,
            @RequestBody ClientDto clientDto) {

        Client updatedClient = clientService.updatePaymentMethods(merchantId, clientDto.getCreditCardEnabled(), clientDto.getQrCodeEnabled(),
                clientDto.getPaypalEnabled(), clientDto.getCryptoEnabled());
        return ResponseEntity.ok(updatedClient);
    }
}
