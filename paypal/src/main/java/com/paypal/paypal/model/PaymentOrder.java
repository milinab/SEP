package com.paypal.paypal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentOrder {
    //private Integer paymentId;
    //private String paymentURL;
    //private Double amount;
    private String status;
    private String payId;
    private String redirectUrl;

    public PaymentOrder(String status) {
        this.status = status;
    }
}

