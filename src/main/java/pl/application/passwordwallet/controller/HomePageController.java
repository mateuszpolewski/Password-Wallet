package pl.application.passwordwallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.application.passwordwallet.model.RegisteredLogin;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.repository.IncorrectLoginsCounterRepository;
import pl.application.passwordwallet.repository.RegisteredLoginRepository;
import pl.application.passwordwallet.repository.UserRepository;
import pl.application.passwordwallet.service.UserAuthenticationService;

import javax.transaction.Transactional;
import java.util.Set;

@Controller
@Transactional
public class HomePageController {
    @Autowired
    UserAuthenticationService userAuthenticationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RegisteredLoginRepository registeredLoginRepository;
    @Autowired
    IncorrectLoginsCounterRepository incorrectLoginsCounterRepository;

    String adminAccount = "admin@admin.pl";

    @GetMapping("/user/home")
    public ModelAndView getHomePage(ModelAndView modelAndView) {
        User user = userAuthenticationService.getCurrentUser();
        RegisteredLogin successfulLogin = registeredLoginRepository.findByUserAndIsSuccessful(user, true);
        RegisteredLogin failedLogin = registeredLoginRepository.findByUserAndIsSuccessful(user, false);

        if(successfulLogin != null)
            modelAndView.addObject("successfulLogin", "Poprawnie zalogowano o czasie: " + successfulLogin.getStringLoginTime() + " z adresu ip: " +  successfulLogin.getIpAddress());
        if(failedLogin != null)
            modelAndView.addObject("failedLogin", "Błędna próba logowania o czasie: " + failedLogin.getStringLoginTime() + " z adresu ip: " +  failedLogin.getIpAddress());
        if (user.getEmail().equals(adminAccount)) {
            modelAndView.addObject("users", userRepository.findAllByActive(false));
            modelAndView.setViewName("/user/adminhome");
        } else
            modelAndView.setViewName("/user/home");
        return modelAndView;
    }

    @GetMapping("/user/home/{id}")
    public ModelAndView unblockUserController(ModelAndView modelAndView, @PathVariable(value = "id") int id) {
        if (!userAuthenticationService.getCurrentUser().getEmail().equals(adminAccount)) {
            modelAndView.setViewName("public/login");
            return modelAndView;
        }

        User userToUnblock = userRepository.getById(id);
        userToUnblock.setActive(true);
        userRepository.save(userToUnblock);
        incorrectLoginsCounterRepository.deleteAllByUser(userToUnblock);
        registeredLoginRepository.deleteAllByUser(userToUnblock);
        modelAndView.addObject("users", userRepository.findAllByActive(false));
        modelAndView.setViewName("/user/adminhome");
        return modelAndView;
    }
}