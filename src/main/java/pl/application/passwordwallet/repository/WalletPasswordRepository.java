package pl.application.passwordwallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.application.passwordwallet.model.WalletPassword;

import java.util.List;

@Repository
public interface WalletPasswordRepository extends JpaRepository<WalletPassword, Integer> {
    @Override
    List<WalletPassword> findAll();

    @Query("SELECT w FROM WalletPassword w INNER JOIN w.user u WHERE u.id = :userId")
    List<WalletPassword> findByUserId(@Param("userId") int userId);

}
