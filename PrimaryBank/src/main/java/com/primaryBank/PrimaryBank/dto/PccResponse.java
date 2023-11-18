package com.primaryBank.PrimaryBank.dto;

import com.primaryBank.PrimaryBank.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PccResponse {
    private Integer acquirerOrderId;
    private LocalDateTime acquirerTimestamp;
    private Integer issuerOrderId;
    private LocalDateTime issuerTimestamp;
    private PaymentStatus paymentStatus;
}
