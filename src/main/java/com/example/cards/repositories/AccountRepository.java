package com.example.cards.repositories;

import com.example.cards.entities.Account;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    //List<UserAccount> findByUserId(User user);
}

