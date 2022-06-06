package pl.application.passwordwallet.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.application.passwordwallet.configuration.HMACPasswordEncoder;
import pl.application.passwordwallet.configuration.SHA512PasswordEncoder;
import pl.application.passwordwallet.model.Password;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.repository.UserRepository;

import javax.validation.constraints.AssertTrue;

import static org.junit.jupiter.api.Assertions.*;
import static pl.application.passwordwallet.service.MyUserDetailsService.constants;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PasswordServiceTest {
    @Autowired
    PasswordService passwordService;
    @Autowired
    SHA512PasswordEncoder sha512PasswordEncoder;
    @Autowired
    HMACPasswordEncoder hmacPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    public void shouldSHA512EncryptionMatch() {
        String rawPassword = "Abcdefg123";
        Password password = passwordService.getEncryptedPassword(rawPassword, "SHA512");
        String salt = password.getSalt();
        String encodedPassword = sha512PasswordEncoder.encode(rawPassword + salt);
        Assertions.assertEquals(encodedPassword, password.getPassword());
    }

    @Test
    public void shouldHMACEncryptionMatch() {
        String rawPassword = "Abcdefg123";
        Password password = passwordService.getEncryptedPassword(rawPassword, "HMAC");
        String salt = password.getSalt();
        String encodedPassword = hmacPasswordEncoder.encode(rawPassword + salt);
        Assertions.assertEquals(encodedPassword, password.getPassword());
    }

    @Test
    public void shouldGenerateDifferentSalt() {
        Assertions.assertNotEquals(passwordService.generateSalt(), passwordService.generateSalt());
    }

    @Test
    public void shouldUpdateUserPassword() {
        User user = userService.findUserByEmail("wojnow@gmail.com");
        String oldTxtPassword = user.getPassword().getPassword();
        passwordService.updateUserPassword(user.getPassword(), "Qwerty001", "HMAC");
        Password userPassword = user.getPassword();
        Assertions.assertNotEquals(oldTxtPassword, userPassword.getPassword());
        Assertions.assertEquals(hmacPasswordEncoder
                        .encode("Qwerty001" + userPassword.getSalt()),
                userPassword.getPassword());

    }


}