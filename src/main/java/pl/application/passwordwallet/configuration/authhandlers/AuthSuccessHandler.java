package pl.application.passwordwallet.configuration.authhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.application.passwordwallet.model.IncorrectLoginsCounter;
import pl.application.passwordwallet.model.RegisteredLogin;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.repository.IncorrectLoginsCounterRepository;
import pl.application.passwordwallet.repository.RegisteredLoginRepository;
import pl.application.passwordwallet.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static pl.application.passwordwallet.service.MyUserDetailsService.constants;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    RegisteredLoginRepository registeredLoginRepository;
    @Autowired
    UserService userService;
    @Autowired
    IncorrectLoginsCounterRepository incorrectLoginsCounterRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        User user = userService.findUserByEmail(constants.getEmail());
        String ipAddress = request.getRemoteAddr();

        RegisteredLogin registeredLogin = registeredLoginRepository.findByUserAndIsSuccessful(user, true);

        if(registeredLogin == null) {
            registeredLoginRepository.save(new RegisteredLogin(ipAddress, true, user));
        } else {
            registeredLogin.updateLoginTime();
            registeredLoginRepository.save(registeredLogin);
        }

        IncorrectLoginsCounter incorrectLoginsCounter = incorrectLoginsCounterRepository.findByIpAddressAndUser(ipAddress, user);
        if(incorrectLoginsCounter != null) {
            incorrectLoginsCounter.setCounter(0);
            incorrectLoginsCounterRepository.save(incorrectLoginsCounter);
        }

        response.sendRedirect("/user/home");
    }
}
