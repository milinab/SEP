package com.primaryBank.PrimaryBank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private String merchantId;
    private String merchantPassword;
    private String pan;

    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Invalid expiration date format (MM/YY)")
    private String expDate;

    @Pattern(regexp = "^\\d{3}$", message = "CVV must be 3 digits")
    private String cvv;

    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Cardholder name must contain only letters and spaces")
    private String cardHolderName;

    private String accountNumber;
    private String name;
}
