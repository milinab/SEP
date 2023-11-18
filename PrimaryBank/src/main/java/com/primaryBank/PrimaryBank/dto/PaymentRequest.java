package com.primaryBank.PrimaryBank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequest {

    private Integer paymentId;
    private String pan;
    private String expDate;
    private String cvv;
    private String cardHolderName;
}
