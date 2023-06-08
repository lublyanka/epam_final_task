package com.example.cards.repositories;

import com.example.cards.entities.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface User repository. */
@Repository
@Hidden
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Exists by email boolean.
   *
   * @param email the email
   * @return the boolean
   */
  boolean existsByEmail(String email);

  /**
   * Find by email user.
   *
   * @param email the email
   * @return the user
   */
  User findByEmail(String email);

  /*    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userAddress a LEFT JOIN FETCH a.country WHERE u.email = :email")
  User findByEmailWithCountry(@Param("email") String email);*/
}
