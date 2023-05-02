package com.example.cards.repositories;

import com.example.cards.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

/*    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userAddress a LEFT JOIN FETCH a.country WHERE u.email = :email")
    User findByEmailWithCountry(@Param("email") String email);*/
}
