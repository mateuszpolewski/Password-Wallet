package pl.application.passwordwallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.application.passwordwallet.configuration.AESenc;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.model.WalletPassword;
import pl.application.passwordwallet.model.WalletPasswordRequest;
import pl.application.passwordwallet.repository.WalletPasswordRequestRepository;
import pl.application.passwordwallet.service.UserAuthenticationService;
import pl.application.passwordwallet.service.WalletPasswordRequestService;
import pl.application.passwordwallet.service.WalletPasswordService;
import pl.application.passwordwallet.util.ModifyStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewWalletPasswords {
    @Autowired
    UserAuthenticationService userAuthenticationService;
    @Autowired
    WalletPasswordService walletPasswordService;
    @Autowired
    WalletPasswordRequestRepository walletPasswordRequestRepository;
    @Autowired
    WalletPasswordRequestService walletPasswordRequestService;
    @Autowired
    ModifyStatus modifyStatus;

    @GetMapping("/user/view-passwords")
    public ModelAndView getUserPasswordsInWallet(ModelAndView modelAndView, @RequestParam(required = false) boolean error) {
        modelAndView.addObject("walletPasswords", walletPasswordService.findCurrentUserWalletPasswords());
        modelAndView.addObject("swapButtonText", getStatusButtonText());
        List<WalletPasswordRequest> requestList = walletPasswordRequestRepository.findAllByUserTo(userAuthenticationService.getCurrentUser());
        List<User> userList = walletPasswordRequestService.generateDistinctUserList(requestList);
        modelAndView.addObject("userList", userList);
        modelAndView.setViewName("/user/walletpasswords");
        if(error)
            modelAndView.addObject("failMessage", "Brak dostępu do danych - włączony tryb read-only lub wybrano udostępnione hasło");
        return modelAndView;

    }
    @GetMapping("/user/view-passwords/{id}")
    public ModelAndView encryptWalletPasswordById(@PathVariable(value="id") int id, ModelAndView modelAndView) {
        List<WalletPassword> walletPasswords = getWalletPasswordsWithOneEncypted(
                walletPasswordService.findCurrentUserWalletPasswords(),
                id);
        modelAndView.addObject("swapButtonText", getStatusButtonText());
        modelAndView.addObject("walletPasswords", walletPasswords);
        modelAndView.setViewName("/user/walletpasswords");
        return modelAndView;
    }
    @PostMapping("/user/swap-mode")
    public RedirectView swapModeHandler() {
        modifyStatus.setReadOnly(!modifyStatus.isReadOnly());
        modifyStatus.isReadOnly();
        return new RedirectView("/user/view-passwords");
    }
    public List<WalletPassword> getWalletPasswordsWithOneEncypted(List<WalletPassword> walletPasswords, int id) {
        User currentUser = userAuthenticationService.getCurrentUser();
        walletPasswords.forEach(walletPassword -> {
            if(walletPassword.getId() == id) {
                try {
                    walletPassword.setHashedPassword(
                            AESenc.decrypt(
                                    walletPassword.getHashedPassword(),
                                    AESenc.generateKey(currentUser.getPassword().getPassword())
                            ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return walletPasswords;
    }

    private String getStatusButtonText() {
        if(modifyStatus.isReadOnly())
            return "Read-only";
        else
            return "Modify mode";
    }

}
