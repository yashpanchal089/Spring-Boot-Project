package com.yash.journalApp.Repository;

import com.yash.journalApp.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    public void testSaveNewUser() {
        Assertions.assertNotNull(userRepositoryImpl.getUserForSA());
    }
}
