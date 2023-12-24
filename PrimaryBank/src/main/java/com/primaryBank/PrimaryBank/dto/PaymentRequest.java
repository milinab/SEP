package com.primaryBank.PrimaryBank.dto;

import com.primaryBank.PrimaryBank.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequest {

    private Integer paymentId;

    @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
    private String pan;

    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Invalid expiration date format (MM/YY)")
    private String expDate;

    @Pattern(regexp = "^\\d{3}$", message = "CVV must be 3 digits")
    private String cvv;

    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Cardholder name must contain only letters and spaces")
    private String cardHolderName;
    private String accountNumber;
    private PaymentType paymentType;
}
