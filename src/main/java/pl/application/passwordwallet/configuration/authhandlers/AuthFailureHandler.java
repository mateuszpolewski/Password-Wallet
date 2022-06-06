package pl.application.passwordwallet.configuration.authhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import pl.application.passwordwallet.model.IncorrectLoginsCounter;
import pl.application.passwordwallet.model.RegisteredLogin;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.repository.IncorrectLoginsCounterRepository;
import pl.application.passwordwallet.repository.RegisteredLoginRepository;
import pl.application.passwordwallet.repository.UserRepository;
import pl.application.passwordwallet.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static pl.application.passwordwallet.service.MyUserDetailsService.constants;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    IncorrectLoginsCounterRepository incorrectLoginsCounterRepository;
    @Autowired
    UserService userService;
    @Autowired
    RegisteredLoginRepository registeredLoginRepository;
    @Autowired
    LoginAttemptService loginAttemptService;
    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String exceptionMessage = exception.getMessage();
        boolean isBlocked = exceptionMessage.equals("5") || exceptionMessage.equals("10");

            User user = userService.findUserByEmail(constants.getEmail());
            String ipAddress = request.getRemoteAddr();

            RegisteredLogin registeredLogin = registeredLoginRepository.findByUserAndIsSuccessful(user, false);
            if (registeredLogin == null) {
                registeredLoginRepository.save(new RegisteredLogin(ipAddress, false, user));
            } else {
                registeredLogin.updateLoginTime();
                registeredLoginRepository.save(registeredLogin);
            }
        if(!isBlocked) {
            IncorrectLoginsCounter incorrectLoginsCounter = incorrectLoginsCounterRepository.findByIpAddressAndUser(ipAddress, user);
            if (incorrectLoginsCounter == null) {
                incorrectLoginsCounter = incorrectLoginsCounterRepository.save(new IncorrectLoginsCounter(ipAddress, 1, user));
            } else {
                incorrectLoginsCounter.setCounter(incorrectLoginsCounter.getCounter() + 1);
                incorrectLoginsCounterRepository.save(incorrectLoginsCounter);
            }

            if (incorrectLoginsCounter.getCounter() == 2) {
                loginAttemptService.loginFailed(ipAddress);
            } else if (incorrectLoginsCounter.getCounter() == 3) {
                loginAttemptService.setLongerCache(true);
                loginAttemptService.loginFailed(ipAddress);
            } else if (incorrectLoginsCounter.getCounter() >= 4){
                loginAttemptService.loginFailed(ipAddress);
                user.setActive(false);
                userRepository.save(user);
            }
        }
        if(isBlocked)
            response.sendRedirect("/login?error=true&blocked="+exceptionMessage);
        else
            response.sendRedirect("/login?error=true");


    }

}