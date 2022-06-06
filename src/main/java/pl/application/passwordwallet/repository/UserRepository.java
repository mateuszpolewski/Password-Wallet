package pl.application.passwordwallet.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.application.passwordwallet.model.User;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Set<User> findAllByActive(boolean active);
}