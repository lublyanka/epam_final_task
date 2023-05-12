package com.example.cards.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/** The User entity table. */
@Entity
@Table(name = "users", schema = "public")
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  @Setter
  private Long id;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "email", nullable = false, unique = true)
  @Getter
  @Setter
  private String email;

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  private String name;

  @Column(name = "surname", nullable = false)
  @Getter
  @Setter
  private String surname;

  @Column(name = "middlename")
  @Getter
  @Setter
  private String middlename;

  @Column(name = "phone", length = 20)
  @Getter
  @Setter
  private String phone;

  @Column(name = "created_on", nullable = false)
  @CreationTimestamp
  @Getter
  @Setter
  private Timestamp createdOn;

  @Column(name = "updated_on")
  @CreationTimestamp
  @Getter
  @Setter
  private Timestamp updatedOn;

  @Column(name = "last_login")
  @Getter
  @Setter
  private Timestamp lastLogin;

  @Column()
  @Getter @Setter
  private Timestamp birthDate;

  @Column(name = "role", length = 10)
  @Getter
  @Setter
  private String role;

  @Column(name = "blocked", columnDefinition = "boolean default false")
  @Getter
  @Setter
  private boolean isBlocked;

  @Getter @Setter @Column private String address;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<Payment> payments;

  /**
   * Gets password.
   *
   * @return the password
   */
  @JsonIgnore
  public String getPassword() {
    return this.password;
  }

  /**
   * Sets password.
   *
   * @param password the password
   */
  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Is user an admin.
   *
   * @return the boolean
   */
  public boolean isAdmin() {
    return getRole().equals("ADMIN");
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof User user)) {
      return false;
    }

    return Objects.equals(this.getId(), user.getId());
  }
}