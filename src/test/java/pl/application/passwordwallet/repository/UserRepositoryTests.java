package pl.application.passwordwallet.repository;

import org.assertj.core.api.Assertions;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.repository.UserRepository;
import pl.application.passwordwallet.service.UserService;

import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    public void saveUserWithPasswordTest() {
        User user = User.builder()
                .name("Wojciech")
                .lastName("Nowak")
                .email("wojnow@gmail.com")
                .chosenEncodingMethod("SHA512")
                .chosenPassword("Abcdefg123")
                .build();

        userService.saveUser(user);

        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    public void getUserTest() {
        User user = userRepository.findById(1).get();
        Assertions.assertThat(user.getId()).isEqualTo(1);
    }


}
