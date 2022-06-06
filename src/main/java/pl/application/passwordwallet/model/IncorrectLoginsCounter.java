package pl.application.passwordwallet.model;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "incorrect_logins_counter")
public class IncorrectLoginsCounter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incorrect_login")
    private int id;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "counter")
    private int counter;
    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;

    public IncorrectLoginsCounter(String ipAddress, int counter, User user) {
        this.id = getId();
        this.ipAddress = ipAddress;
        this.counter = counter;
        this.user = user;
    }
}
