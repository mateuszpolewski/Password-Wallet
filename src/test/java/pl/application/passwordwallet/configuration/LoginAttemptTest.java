package pl.application.passwordwallet.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.application.passwordwallet.configuration.authhandlers.LoginAttemptService;

import javax.validation.constraints.AssertTrue;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class LoginAttemptTest {
    @Autowired
    LoginAttemptService loginAttemptService;

    private String ipAddress = "127.0.0.1";

    @Test
    public void shouldCreateCache() {
        try {
            loginAttemptService.createCache(5, TimeUnit.SECONDS);
            loginAttemptService.createCache(30, TimeUnit.SECONDS);
            loginAttemptService.createCache(100, TimeUnit.SECONDS);
            loginAttemptService.createCache(1, TimeUnit.HOURS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cache5sshouldBeNotExpired() {
        loginAttemptService.loginFailed(ipAddress);
        Assertions.assertTrue(loginAttemptService.getCache5s().getIfPresent(ipAddress));
    }

    @Test
    public void cache10sshouldBeNotExpired() {
        loginAttemptService.setLongerCache(true);
        loginAttemptService.loginFailed(ipAddress);
        Assertions.assertTrue(loginAttemptService.getCache10s().getIfPresent(ipAddress));
    }

    @Test
    public void shouldBeBlocked() {
        Assertions.assertTrue(loginAttemptService.isBlocked(ipAddress));
    }
}
