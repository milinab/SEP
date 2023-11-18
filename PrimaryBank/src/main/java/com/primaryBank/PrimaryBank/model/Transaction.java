package com.primaryBank.PrimaryBank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "transactions")
@Table(name = "transactions")
public class Transaction {
    @Generated
    @Id
    private Integer paymentId;
    private Integer merchantOrderId;
    private String merchantId;
    private double amount;
    private LocalDateTime merchantTimeStamp;

    public Transaction(Integer merchantOrderId, String merchantId, Double amount, LocalDateTime merchantTimeStamp) {
    }
}
