package com.psp.PSPBackend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionStartsDTO {
    private String merchantId;
    private Double amount;
    private Integer merchantOrderId;
}
