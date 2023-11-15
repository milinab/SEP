package com.secondaryBank.SecondaryBank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Generated
    @Id
    private String id;
    private double availableSum;
    private String pan;
    private String expDate;
    private String cvv;
    private String name;
}
