package com.psp.PSPBackend.model;

import com.psp.PSPBackend.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private Integer paymentId;
    private Long merchantOrderId;
    private String merchantId;
    private Double amount;
    private LocalDateTime merchantTimeStamp;
    private PaymentStatus paymentStatus;
}
