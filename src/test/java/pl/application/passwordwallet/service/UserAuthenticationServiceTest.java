package pl.application.passwordwallet.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.application.passwordwallet.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAuthenticationServiceTest {
    @Autowired
    UserAuthenticationService userAuthenticationService;

    @BeforeEach
    public void setupUser() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "wojnow@gmail.com",
                        "Qwerty001",
                        createAuthorityList("user")));
    }
    /*
    @Test
    void shouldReturnCurrentUser() {
        User user = userAuthenticationService.getCurrentUser();
        System.out.println(user.getEmail());
        assertAll("User data should be correct",
                () -> {
                    Assertions.assertThat(user.getId()).isGreaterThan(0);
                },
                () -> {
                    Assertions.assertThat(user.getEmail()).isEqualTo("wojnow@gmail.com");
                },
                () -> {
                    Assertions.assertThat(user.getPassword().getId()).isGreaterThan(0);
                });
    }

     */
}