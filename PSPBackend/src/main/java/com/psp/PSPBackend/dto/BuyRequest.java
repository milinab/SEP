package com.psp.PSPBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BuyRequest {
    private String merchantId;
    private Double amount;
    // i jos preko cega ide placanje za sad samo kartica
}
