package pl.application.passwordwallet.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.application.passwordwallet.model.Password;


@Repository
public interface PasswordRepository extends JpaRepository<Password, Integer> {
    Password findById(int id);
}
