package pl.application.passwordwallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pl.application.passwordwallet.configuration.authhandlers.LoginAttemptService;
import pl.application.passwordwallet.model.Role;
import pl.application.passwordwallet.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Component
    public static class Constants {
        private String salt;
        private String email;
        public void setSalt(String salt) {
            this.salt = salt;
        }
        public String getSalt() {
            return this.salt;
        }
        public void setEmail(String email) {this.email = email; }
        public String getEmail() { return this.email; }
    }

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private static HttpServletRequest request;

    public static Constants constants = new Constants();

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String ip = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();

        if (loginAttemptService.isBlocked(ip)) {
            if(!loginAttemptService.isLongerCache())
                throw new RuntimeException("5");
            else if(loginAttemptService.isLongerCache())
                throw new RuntimeException("10");

        }

        User user = userService.findUserByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException(email);
        }
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        constants.setSalt(user.getPassword().getSalt());
        constants.setEmail(user.getEmail());
        return buildUserForAuthentication(user, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new ArrayList<>(roles);
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                passwordService.getById(user.getId()).getPassword(),
                user.getActive(),
                true,
                true,
                true,
                authorities);
    }

    public static String getClientIP() {

        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }


}