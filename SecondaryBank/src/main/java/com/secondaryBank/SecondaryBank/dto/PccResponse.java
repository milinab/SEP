package com.secondaryBank.SecondaryBank.dto;

import com.secondaryBank.SecondaryBank.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
