package com.crypto.cryptoApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class CoingateRequest {
    @JsonProperty("price_amount")
    private double priceAmount;

//    @JsonProperty("success_url")
//    private String successUrl;
//
//    @JsonProperty("cancel_url")
//    private String cancelUrl;

    @JsonProperty("price_currency")
    private String priceCurrency;

    @JsonProperty("receive_currency")
    private String receiveCurrency;
}
