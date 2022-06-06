package pl.application.passwordwallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.model.WalletPassword;
import pl.application.passwordwallet.model.WalletPasswordRequest;

import java.util.List;

@Repository
public interface WalletPasswordRequestRepository extends JpaRepository<WalletPasswordRequest, Integer> {
    List<WalletPasswordRequest> findAllByUserTo(User userTo);

    List<WalletPasswordRequest> findAllByUserFromAndUserTo(User userFrom, User currUser);
}
