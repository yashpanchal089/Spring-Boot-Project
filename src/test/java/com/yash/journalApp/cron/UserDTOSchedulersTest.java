package com.yash.journalApp.cron;

import com.yash.journalApp.schedular.UserSchedular;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDTOSchedulersTest {

    @Autowired
    private UserSchedular userScheduler;

    @Test
    public void testFetchUsersAndSendSaMail() {
        userScheduler.fetchUsersAndSendSaMail();
    }

}
