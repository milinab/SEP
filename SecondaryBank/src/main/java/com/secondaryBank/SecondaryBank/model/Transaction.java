package com.secondaryBank.SecondaryBank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "transactions")
@Table(name = "transactions")
public class Transaction {

    @Id
    private Integer merchantOrderId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private Client client;
    private double amount;
    private LocalDateTime merchantTimeStamp;

}
