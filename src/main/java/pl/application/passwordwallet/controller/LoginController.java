package pl.application.passwordwallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.repository.UserRepository;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static pl.application.passwordwallet.service.MyUserDetailsService.constants;


@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @GetMapping(value="/login")
    public ModelAndView getLoginForm(ModelAndView modelAndView, @RequestParam(required = false) boolean error, @RequestParam(required = false,defaultValue="0") int blocked) throws Exception {
       modelAndView.setViewName("/public/login");
       User user = userRepository.findByEmail(constants.getEmail());

       if(user!= null) {
           if (!user.getActive())
               modelAndView.addObject("failMessage", "Konto zablokowane - Proszę o kontakt z administratorem");
           else if (error && blocked > 0)
               modelAndView.addObject("failMessage", "Konto zostało zablokowane na czas " + blocked + " sekund");
           else if (error)
               modelAndView.addObject("failMessage", "Błędne hasło");
       } else if(error)
           modelAndView.addObject("failMessage", "Nie znaleziono użytkownika");
       
       return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView getLoginExceptionForm(ModelAndView modelAndView, RuntimeException re) throws Exception {
        modelAndView.setViewName("/public/login");
            modelAndView.addObject("failMessage", "re.getMessage()");


        return modelAndView;
    }
}
