package com.primaryBank.PrimaryBank.repository;

import com.primaryBank.PrimaryBank.model.Card;
import com.primaryBank.PrimaryBank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> {
    Card findCardByPan(String pan);
}
