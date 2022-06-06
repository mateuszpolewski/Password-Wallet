package pl.application.passwordwallet.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @Test
    void shouldFindRoleByRoleName() {
        Assertions.assertThat(roleRepository.findByRole("USER").getId()).isGreaterThan(0);
    }

    @Test
    void shouldFindAllRoles() {
        assertNotNull(roleRepository.findAll());
    }
}