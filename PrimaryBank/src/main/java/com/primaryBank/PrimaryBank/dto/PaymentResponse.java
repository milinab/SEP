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
public class PaymentResponse {
    private Long merchantOrderId;
    private Integer acquirerOrderId;
    private LocalDateTime acquirerTimestamp;
    private PaymentStatus paymentStatus;
    private Integer issuerOrderId;
}
