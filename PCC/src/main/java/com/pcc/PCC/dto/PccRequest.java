package com.pcc.PCC.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PccRequest {
    private String pan;
    private String expDate;
    private String cvv;
    private String cardHolderName;
    private Integer acquiererOrderId;
    private LocalDateTime acquiererTimestamp;
    private Double amount;
    private String accountNumber;
}
