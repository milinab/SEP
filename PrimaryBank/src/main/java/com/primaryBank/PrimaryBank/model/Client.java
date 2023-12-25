package com.primaryBank.PrimaryBank.model;

import com.primaryBank.PrimaryBank.dto.ClientDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "clients")
@Table(name = "clients")
public class Client {

    public Client(ClientDto dto) {
        this.merchantId = dto.getMerchantId();
        this.merchantPassword = dto.getMerchantPassword();
        this.pan = dto.getPan();
        this.expDate = dto.getExpDate();
        this.cvv = dto.getCvv();
        this.cardHolderName = dto.getCardHolderName();
        this.accountNumber = dto.getAccountNumber();
        this.name = dto.getName();
    }


    @Id
    private String merchantId;
    private String merchantPassword;
    private double availableSum;

    @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
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
