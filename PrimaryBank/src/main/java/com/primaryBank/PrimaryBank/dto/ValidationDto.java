package com.primaryBank.PrimaryBank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidationDto {
    private boolean isValid;
    private Integer issuerTransactionId;
}
