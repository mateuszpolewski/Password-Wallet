package pl.application.passwordwallet.service;

import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import pl.application.passwordwallet.configuration.AESenc;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.model.WalletPassword;
import pl.application.passwordwallet.repository.WalletPasswordRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WalletPasswordServiceTest {
    @Autowired
    WalletPasswordService walletPasswordService;
    @Autowired
    PasswordService passwordService;
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

    @Test
    void saveWalletPasswordTest() throws Exception {
        WalletPassword walletPassword = WalletPassword.builder()
                .title("Abcdef")
                .description("")
                .hashedPassword("Zxcvbnm123")
                .link("asdadaddd00")
                .build();

        walletPasswordService.saveWalletPassword(walletPassword);
        String email = userAuthenticationService.getCurrentUser().getEmail();
        System.out.println(email);
        Assertions.assertThat(walletPassword.getId()).isGreaterThan(0);
    }

    @Test
    void shouldFindCurrentUserWalletPasswords() {
        walletPasswordService.findCurrentUserWalletPasswords();
        assertNotNull(walletPasswordService.findCurrentUserWalletPasswords());
    }

    @Test
    void shouldUpdateWalletPasswords() {
        User user = userAuthenticationService.getCurrentUser();
        String encryptedOldMasterPassword = user.getPassword().getPassword();
        ArrayList<String> oldPasswordsHashes =  getUserHashedWalletPasswordList(walletPasswordService.findCurrentUserWalletPasswords());
        passwordService.updateUserPassword(user.getPassword(), "Wpwpwp555", "HMAC");
        walletPasswordService.updateWalletPasswords(encryptedOldMasterPassword, "Wpwpwp555");
        ArrayList<String> newPasswordsHashes =  getUserHashedWalletPasswordList(walletPasswordService.findCurrentUserWalletPasswords());
        //assertArrayEquals(Arrays.asList(oldPasswordsHashes).toArray(),Arrays.asList(newPasswordsHashes).toArray());
        //nie gotowe
    }

    private ArrayList<String> getUserHashedWalletPasswordList(List<WalletPassword> walletPasswords) {
        ArrayList<String> list = new ArrayList<>();
        walletPasswords.forEach(walletPassword ->
                list.add(walletPassword.getHashedPassword())
                );
        return list;
    }

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

    //@AfterEach
    //public void clearSecurityContext() {
    //    SecurityContextHolder.clearContext();
    //}
}