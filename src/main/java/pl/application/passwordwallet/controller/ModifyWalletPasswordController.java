package pl.application.passwordwallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.application.passwordwallet.model.WalletPassword;
import pl.application.passwordwallet.repository.WalletPasswordRepository;
import pl.application.passwordwallet.util.ModifyStatus;

@Controller
public class ModifyWalletPasswordController {
    @Autowired
    WalletPasswordRepository walletPasswordRepository;
    @Autowired
    ModifyStatus modifyStatus;

    @GetMapping("/edit-wallet-password/{id}")
    public ModelAndView editWalletPassword(ModelAndView modelAndView, @PathVariable(value="id") int id) {
        WalletPassword walletPassword = walletPasswordRepository.findById(id).get();

        if(modifyStatus.isReadOnly() || walletPassword.getSharedBy() != null) {
            modelAndView = new ModelAndView("redirect:/user/view-passwords");
            modelAndView.addObject("error", true);
            return  modelAndView;
        }
        modelAndView.addObject("walletPassword", walletPassword);
        modelAndView.setViewName("/user/editpassword");
        return modelAndView;
    }

    @GetMapping("/delete-wallet-password/{id}")
    public ModelAndView deleteWalletPassword(ModelAndView modelAndView, @PathVariable(value="id") int id) {
        modelAndView = new ModelAndView("redirect:/user/view-passwords");
        WalletPassword walletPassword = walletPasswordRepository.findById(id).get();

        if(modifyStatus.isReadOnly() || walletPassword.getSharedBy() != null) {
            modelAndView.addObject("error", true);
            return  modelAndView;
        }
        walletPasswordRepository.delete(walletPassword);
        return modelAndView;
    }

}
