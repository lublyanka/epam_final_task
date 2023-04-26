package com.example.cards.repositories;

import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findAllByUser(User user);
}
