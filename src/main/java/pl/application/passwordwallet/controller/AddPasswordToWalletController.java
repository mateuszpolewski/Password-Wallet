package pl.application.passwordwallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.application.passwordwallet.model.WalletPassword;
import pl.application.passwordwallet.service.WalletPasswordService;

import javax.validation.Valid;

@Controller
public class AddPasswordToWalletController {
    @Autowired
    WalletPasswordService walletPasswordService;

    @GetMapping("/user/add-password")
    public ModelAndView addWalletPasswordTemplate(ModelAndView modelAndView) {
        modelAndView.addObject("walletPassword", new WalletPassword());
        modelAndView.setViewName("/user/addpassword");
        return modelAndView;
    }

    @PostMapping("/user/add-password")
    public ModelAndView addWalletPassword(ModelAndView modelAndView, @Valid WalletPassword walletPassword,
                                          BindingResult bindingResult) throws Exception {
        if(!bindingResult.hasErrors()) {
            walletPasswordService.saveWalletPassword(walletPassword);
            modelAndView.addObject("walletPassword", new WalletPassword());
            modelAndView.addObject("successMessage", "Poprawnie dodano hasło.");
        }
        modelAndView.setViewName("/user/addpassword");
        return modelAndView;
    }
}
