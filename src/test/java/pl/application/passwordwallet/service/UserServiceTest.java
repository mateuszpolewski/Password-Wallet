package pl.application.passwordwallet.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.application.passwordwallet.model.User;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void shouldFindUserByEmail() {
        User user = userService.findUserByEmail("wojnow@gmail.com");
        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }


}
