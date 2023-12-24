package com.apiGateway.apiGateway.dto;

import com.apiGateway.apiGateway.enums.PaymentStatus;
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
