package com.example.cards.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "invoices", schema = "public")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Lob
    //@JdbcTypeCode(Types.BINARY)
    @Column(name = "doc")
    private byte[] binaryData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    public UUID getId() {
        return id;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

}
