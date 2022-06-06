package pl.application.passwordwallet.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.application.passwordwallet.model.Password;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.service.PasswordService;
import pl.application.passwordwallet.service.UserService;

import javax.annotation.PostConstruct;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PasswordRepositoryTest {
    private static UserService userServiceHelper;
    @Autowired
    UserService userService;
    @Autowired
    PasswordRepository passwordRepository;
    @Autowired
    UserRepository userRepository;

    /*
    @PostConstruct
    public void init() {
        PasswordRepositoryTest.userServiceHelper = userService;
    }
    @BeforeAll
    public static void saveUserWithPassword() {
        User user = User.builder()
                .name("Jan")
                .lastName("Kowalski")
                .email("jankow@gmail.com")
                .chosenEncodingMethod("SHA512")
                .chosenPassword("Abcdefg123")
                .build();

        userServiceHelper.saveUser(user);
        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }
    */
    @Test
    public void shouldFindPasswordById() {
        //Assertions.assertThat(passwordRepository.findById(1).getId()).isGreaterThan(0);
    }

}