package com.example.cards.repositories;

import com.example.cards.entities.Account;
import com.example.cards.entities.User;
import java.util.UUID;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Query("SELECT a FROM Account a LEFT JOIN FETCH UserAccount ua on a = ua.userAccountKey.accountId WHERE ua.userAccountKey.userId = :user")
    Page<Account> findByUserIdWithPagination(@Param("user") User user, Pageable pageable);
}

