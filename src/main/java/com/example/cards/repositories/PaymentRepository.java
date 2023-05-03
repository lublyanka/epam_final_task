package com.example.cards.repositories;

import com.example.cards.entities.Account;
import com.example.cards.entities.Payment;
import com.example.cards.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;

import jakarta.persistence.criteria.Join;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findAllByUser_Id(Long userId, Pageable pageable);

    Page<Payment> findAllByAccount(Account account, Pageable pageable);


}
