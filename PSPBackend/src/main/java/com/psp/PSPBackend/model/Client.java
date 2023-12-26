package com.psp.PSPBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "clients")
@Table(name = "clients")
public class Client {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String merchantId;
    private String merchantPassword;
    private Boolean creditCardEnabled;
    private Boolean qrCodeEnabled;
    private Boolean paypalEnabled;
    private Boolean cryptoEnabled;
}
