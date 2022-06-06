package pl.application.passwordwallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.service.UserService;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    UserService userService;

    @GetMapping(value="/register")
    public ModelAndView newRegistrationForm(ModelAndView modelAndView ){
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("/public/registration");
        return modelAndView;
    }

    @PostMapping(value="/register")
    public ModelAndView registerUserInDatabase(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, ModelAndView modelAndView) {
        if(userService.findUserByEmail(user.getEmail()) != null) {
            bindingResult.addError(new FieldError("user", "email",
                    "Istnieje użytkownik z takim adresem e-mail"));
        }
        if(!bindingResult.hasErrors()) {
            userService.saveUser(user);
            modelAndView.addObject("user", new User());
            modelAndView.addObject("successMessage", "Poprawnie zarejstrowano użytkownika.");
        }
        modelAndView.setViewName("/public/registration");
        return modelAndView;
    }
}
