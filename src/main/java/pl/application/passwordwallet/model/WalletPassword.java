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
@Table(name = "wallet_password")
public class WalletPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_password_id")
    private int id;
    @Column(name = "hashed_password")
    private String hashedPassword;
    @Column(name = "title")
    private String title;
    @Column(name = "link")
    private String link;
    @Column(name = "description")
    private String description;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "shared_by_user_id")
    private User sharedBy;

    public WalletPassword(String hashedPassword, String title, String link, String description, User user, User sharedBy) {
        this.id = getId();
        this.hashedPassword = hashedPassword;
        this.title = title;
        this.link = link;
        this.description = description;
        this.user = user;
        this.sharedBy = sharedBy;
    }
}
