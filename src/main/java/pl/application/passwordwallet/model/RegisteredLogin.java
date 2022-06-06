package pl.application.passwordwallet.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;

import javax.persistence.*;

import static pl.application.passwordwallet.service.MyUserDetailsService.constants;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "registered_logins")
public class RegisteredLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "is_successful")
    private boolean isSuccessful;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "login_time")
    private Calendar loginTime;

    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;

    public RegisteredLogin(String ipAddress, boolean isSuccessful, User user) {

        this.id = getId();
        this.ipAddress = ipAddress;
        this.isSuccessful = isSuccessful;
        this.loginTime = getLoginTime();
        this.user = user;
    }

    public void updateLoginTime() {
        this.loginTime = Calendar.getInstance();
    }

    public String getStringLoginTime() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar loginTimeTemp = loginTime;
        loginTimeTemp.add(Calendar.HOUR_OF_DAY, -1);
        String dateString = format1.format(loginTimeTemp.getTime());
        return dateString;
    }
}
