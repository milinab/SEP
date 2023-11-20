package com.pcc.PCC.model;

import com.pcc.PCC.enums.PaymentStatus;
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
    private Integer acquirerOrderId;
    private LocalDateTime acquirerTimestamp;
    private Integer issuerOrderId;
    private LocalDateTime issuerTimestamp;
    private PaymentStatus paymentStatus;
}
