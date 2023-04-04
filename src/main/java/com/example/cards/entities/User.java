package com.example.cards.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", length = 15, nullable = false, unique = true)
    private String login;

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
    private Timestamp createdOn;

    @Column(name = "update_on")
    private Timestamp updateOn;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_add_id")
    private UserAddress userAddress;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserDocument> userDocuments = new ArrayList<>();

}