package pl.application.passwordwallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.application.passwordwallet.model.RegisteredLogin;
import pl.application.passwordwallet.model.User;

@Repository
public interface RegisteredLoginRepository extends JpaRepository<RegisteredLogin, Integer> {
    RegisteredLogin findByIpAddressAndUser(String ipAddress, User user);
    RegisteredLogin findByIpAddress(String ipAddress);
    RegisteredLogin findByUserAndIsSuccessful(User user, boolean isSuccessful);
    void deleteAllByUser(User user);
}
