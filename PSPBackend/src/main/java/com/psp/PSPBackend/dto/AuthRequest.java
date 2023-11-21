package com.psp.PSPBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequest {

    private String merchantId;
    private String merchantPassword;
    private Double amount;
    private Long merchantOrderId;
    private LocalDateTime merchantTimeStamp;
}
