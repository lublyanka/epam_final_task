package com.example.cards.utils;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HibernateUtilTest {
    @Autowired
    private SessionFactory sessionFactory;

    @BeforeAll
    public void setup() {
        // Any additional setup logic if needed
    }

    @AfterAll
    public void teardown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void getSession_WithValidSessionFactory_ReturnsSession() {
        Session session = sessionFactory.openSession();
        assertNotNull(session);
    }
}