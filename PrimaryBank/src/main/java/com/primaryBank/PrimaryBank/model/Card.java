package com.primaryBank.PrimaryBank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cards")
@Table(name = "cards")
public class Card {

    @Id
    private String id;
    private String pan;
    private String expDate;
    private String cvv;
    private String cardHolderName;
}
