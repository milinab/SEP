package com.primaryBank.PrimaryBank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cards")
@Table(name = "cards")
public class Card {

    @Id
    private String id;

    @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
    private String pan;

    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Invalid expiration date format (MM/YY)")
    private String expDate;

    @Pattern(regexp = "^\\d{3}$", message = "CVV must be 3 digits")
    private String cvv;

    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Cardholder name must contain only letters and spaces")
    private String cardHolderName;
}
