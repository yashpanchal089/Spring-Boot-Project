package com.yash.journalApp.service;

import com.yash.journalApp.entity.User;
import com.yash.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDTOServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @ParameterizedTest
    @ArgumentsSource(UserArgumentProvider.class)
    public void testSaveNewUser(User user){
        assertTrue(userService.saveNewUser(user));
    }


    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,10,12",
            "3,3,9"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a + b);
    }

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {
            "ram",
            "laxman",
            "yash",
            "sachin",
    })
    public void testFindByUserName(String name){
        assertNotNull(userRepository.findByUserName(name));
        assertEquals(6, 3+3);
        assertTrue(5>3);
    }
}
