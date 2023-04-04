package com.example.cards.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "user_document_ids", schema = "public")
public class UserDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", length = 50,nullable = false, unique = true)
    private String number;

    @Column(name = "valid_from")
    private Date validFrom;

    @Column(name = "valid_to")
    private Date validTo;

    @Column(name = "authority")
    private String authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;


    public void setNumber(String number) {
        this.number = number;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public String getAuthority() {
        return authority;
    }

    public User getUserId() {
        return userId;
    }

}