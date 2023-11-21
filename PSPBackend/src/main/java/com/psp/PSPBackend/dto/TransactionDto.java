package com.psp.PSPBackend.dto;

import com.psp.PSPBackend.enums.PaymentStatus;
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
    private Integer merchantOrderId;
    private String merchantId;
    private double amount;
    private LocalDateTime merchantTimeStamp;
    private PaymentStatus paymentStatus;
    private LocalDateTime acquiererTimestamp;
    private LocalDateTime issuerTimestamp;
}
