package com.example.cards.repositories;

import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findAllByUser(User user);
}
