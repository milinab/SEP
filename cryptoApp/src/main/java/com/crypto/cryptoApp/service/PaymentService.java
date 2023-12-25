package com.crypto.cryptoApp.service;

import com.crypto.cryptoApp.dto.CoingateRequest;
import com.crypto.cryptoApp.dto.CryptoPayingRequest;
import com.crypto.cryptoApp.dto.CryptoPayingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PaymentService {
    @Value("${crypto.exchange.api.key}")
    private String apiKey;

    @Value("${crypto.exchange.api.url}")
    private String apiUrl;

    public CryptoPayingResponse pay(CryptoPayingRequest cryptoPayingRequest) {
        CryptoPayingResponse response = new CryptoPayingResponse();
        response.setPaymentUrl(callCryptoExchangeApi(cryptoPayingRequest));
        return response;
    }

    private String callCryptoExchangeApi(CryptoPayingRequest cryptoPayingRequest) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        System.out.println(apiKey + " " + apiUrl);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CoingateRequest request = new CoingateRequest();

        request.setPriceAmount(cryptoPayingRequest.getAmount());
        request.setPriceCurrency("USD");
        request.setReceiveCurrency("USD");

        HttpEntity<CoingateRequest> requestEntity = new HttpEntity<>(request, headers);
        System.out.println(requestEntity);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(apiUrl + "/orders", requestEntity, Map.class);
        System.out.println(responseEntity.getBody());

        Map<String, Object> responseBody = responseEntity.getBody();
        System.out.println(responseBody);

        String paymentUrl = (String) responseBody.get("payment_url");
        System.out.println(paymentUrl);
        return paymentUrl;
    }

}
