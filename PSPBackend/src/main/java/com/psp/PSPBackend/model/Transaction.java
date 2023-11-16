package com.psp.PSPBackend.model;

import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class Transaction {

    @Id
    private Integer merchantOrderId;
    private String merchantId;
    private String clientId;
    private Double amount;
    private LocalDateTime merchantTimeStamp;
}
