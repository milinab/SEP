package com.primaryBank.PrimaryBank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "clients")
@Table(name = "clients")
public class Client {

    @Id
    private String merchantId;
    private String merchantPassword;
    private double availableSum;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id")
    private Card card;
}
