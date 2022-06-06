package pl.application.passwordwallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.application.passwordwallet.model.User;

@Service
public class UserAuthenticationService {
    @Autowired
    UserService userService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalEmail = authentication.getName();
        return userService.findUserByEmail(currentPrincipalEmail);

    }
}
