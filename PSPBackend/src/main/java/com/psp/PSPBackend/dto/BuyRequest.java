package com.psp.PSPBackend.dto;

import com.psp.PSPBackend.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuyRequest {
    private String merchantId;
    private Double amount;
    private Integer merchantOrderId;
    private PaymentType paymentType;
}
