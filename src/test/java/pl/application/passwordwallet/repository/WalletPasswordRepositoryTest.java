package pl.application.passwordwallet.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.application.passwordwallet.service.WalletPasswordService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WalletPasswordRepositoryTest {
    @Autowired
    WalletPasswordRepository walletPasswordRepository;

    @Test
    void shouldFindAllWalletPasswords() {
        Assertions.assertNotNull(walletPasswordRepository.findAll());
    }

    @Test
    void shouldFindWalletPasswordByUserId() {
        Assertions.assertNotNull(walletPasswordRepository.findByUserId(1));
    }
}