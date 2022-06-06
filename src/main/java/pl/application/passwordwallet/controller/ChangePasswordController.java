package pl.application.passwordwallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.application.passwordwallet.model.Password;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.service.PasswordService;
import pl.application.passwordwallet.service.UserAuthenticationService;
import pl.application.passwordwallet.service.WalletPasswordService;
import pl.application.passwordwallet.util.PasswordConfirmation;

import javax.validation.Valid;

@Controller
public class ChangePasswordController {
    @Autowired
    PasswordService passwordService;
    @Autowired
    UserAuthenticationService userAuthenticationService;
    @Autowired
    WalletPasswordService walletPasswordService;

    @GetMapping("/user/change-password")
    public ModelAndView getChangePasswordForm(ModelAndView modelAndView) {
        modelAndView.setViewName("/user/changepassword");
        modelAndView.addObject("confirmPassword", new PasswordConfirmation());
        return modelAndView;
    }

    @PostMapping("/user/change-password")
    public ModelAndView confirmPasswordChange(@Valid @ModelAttribute("confirmPassword") PasswordConfirmation passwordConfirmation,
                                              BindingResult bindingResult, ModelAndView modelAndView) {
        boolean arePasswordsMatch = passwordConfirmation.getFirstPassword().equals(passwordConfirmation.getSecondPassword());
        User user = userAuthenticationService.getCurrentUser();

        String encryptedOldMasterPassword = user.getPassword().getPassword();

        System.out.println(encryptedOldMasterPassword);

        if(!arePasswordsMatch)
            modelAndView.addObject("passwordMatch", "Hasła się nie zgadzają");

        if(!bindingResult.hasErrors() && arePasswordsMatch) {
            passwordService.updateUserPassword(
                    user.getPassword(),
                    passwordConfirmation.getFirstPassword(),
                    passwordConfirmation.getEncodingMethod()
            );
            System.out.println(encryptedOldMasterPassword);

            walletPasswordService.updateWalletPasswords(encryptedOldMasterPassword, user.getPassword().getPassword());
            modelAndView.addObject("successMessage", "Poprawnie zmieniono hasło");
        }

        modelAndView.setViewName("/user/changepassword");
        return modelAndView;
    }

}