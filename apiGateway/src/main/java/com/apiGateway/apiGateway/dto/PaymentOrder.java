package com.apiGateway.apiGateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentOrder {
    private String status;
    private String payId;
    private String redirectUrl;
}

