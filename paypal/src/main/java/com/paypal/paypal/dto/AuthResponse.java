package com.paypal.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private Integer paymentId;
    private String paymentURL;
    private Double amount;

}