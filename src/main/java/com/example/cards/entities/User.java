package com.example.cards.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users", schema = "public")
public class User implements UserDetails, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /*   @Column(name = "login", length = 15, nullable = false, unique = true)
  private String login;*/

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

  @Column(name = "role", length = 10)
  @Getter
  @Setter
  private String role;

  @Column(name = "isblocked", columnDefinition = "boolean default false")
  private boolean isBlocked;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_add_id")
  private UserAddress userAddress;

  @OneToMany(cascade = CascadeType.ALL)
  private List<UserDocument> userDocuments = new ArrayList<>();

  public Long getId() {
    return id;
  }

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + getRole()));
    return authorities;
  }

  @JsonIgnore
  public String getPassword() {
    return this.password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !this.isBlocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return !this.isBlocked;
  }

  @JsonIgnore
  public void setBlocked(Boolean blocked) {
    isBlocked = blocked;
  }

  public UserAddress getUserAddress() {
    return userAddress;
  }

  public void setUserAddress(UserAddress userAddress) {
    this.userAddress = userAddress;
  }

  public List<UserDocument> getUserDocuments() {
    return userDocuments;
  }

  public void setUserDocuments(List<UserDocument> userDocuments) {
    this.userDocuments = userDocuments;
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
