package com.primaryBank.PrimaryBank.dto;

import com.primaryBank.PrimaryBank.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionDto {
    private Integer paymentId;
    private Long merchantOrderId;
    private String merchantId;
    private double amount;
    private LocalDateTime merchantTimeStamp;
    private PaymentStatus paymentStatus;
    private LocalDateTime acquiererTimestamp;
    private LocalDateTime issuerTimestamp;
}
