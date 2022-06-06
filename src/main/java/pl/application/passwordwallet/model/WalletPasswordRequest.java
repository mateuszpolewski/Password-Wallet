package pl.application.passwordwallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallet_password_request")
public class WalletPasswordRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_password_request_id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_from_user_id")
    private User userFrom;
    @ManyToOne
    @JoinColumn(name = "user_to_user_id")
    private User userTo;
    @ManyToOne
    @JoinColumn(name = "wallet_password_wallet_password_id")
    private WalletPassword walletPassword;

    public WalletPasswordRequest(User userFrom, User userTo, WalletPassword walletPassword) {
        this.id = getId();
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.walletPassword = walletPassword;
    }
}
