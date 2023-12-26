package com.psp.PSPBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientDto {
    private Boolean creditCardEnabled;
    private Boolean qrCodeEnabled;
    private Boolean paypalEnabled;
    private Boolean cryptoEnabled;
}
