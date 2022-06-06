package pl.application.passwordwallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.application.passwordwallet.model.IncorrectLoginsCounter;
import pl.application.passwordwallet.model.User;


@Repository
public interface IncorrectLoginsCounterRepository extends JpaRepository<IncorrectLoginsCounter, Integer> {
    IncorrectLoginsCounter findByIpAddressAndUser(String ipAddress, User user);
    IncorrectLoginsCounter findByIpAddress(String ipAddress);
    void deleteAllByUser(User user);
}
