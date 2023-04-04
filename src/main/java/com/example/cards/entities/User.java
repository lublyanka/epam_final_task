package com.example.cards.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users", schema = "public")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 /*   @Column(name = "login", length = 15, nullable = false, unique = true)
    private String login;*/

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "middlename")
    private String middlename;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "created_on", nullable = false)
    @CreationTimestamp
    private Timestamp createdOn;

    @Column(name = "update_on")
    @CreationTimestamp
    private Timestamp updateOn;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_add_id")
    private UserAddress userAddress;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserDocument> userDocuments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public String getPassword() {
        return this.password;
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
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getPhone() {
        return phone;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public Timestamp getUpdateOn() {
        return updateOn;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public List<UserDocument> getUserDocuments() {
        return userDocuments;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserDocuments(List<UserDocument> userDocuments) {
        this.userDocuments = userDocuments;
    }
}