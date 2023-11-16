package com.primaryBank.PrimaryBank.dto;

import java.time.LocalDateTime;
public class AuthRequest {

    private String merchantId;
    private String merchantPassword;
    private Double amount;
    private Integer merchantOrderId;
    private LocalDateTime merchantTimeStamp;
}
