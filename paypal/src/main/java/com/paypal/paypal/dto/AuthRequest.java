package com.paypal.paypal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequest {

    private String merchantId;
    private String merchantPassword;
    private Double amount;
    private Integer merchantOrderId;
    private LocalDateTime merchantTimeStamp;
}
