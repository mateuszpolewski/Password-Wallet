package pl.application.passwordwallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.model.WalletPassword;
import pl.application.passwordwallet.model.WalletPasswordRequest;
import pl.application.passwordwallet.repository.UserRepository;
import pl.application.passwordwallet.repository.WalletPasswordRepository;
import pl.application.passwordwallet.repository.WalletPasswordRequestRepository;
import pl.application.passwordwallet.service.UserAuthenticationService;
import pl.application.passwordwallet.service.WalletPasswordService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SharePasswordController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    WalletPasswordRepository walletPasswordRepository;
    @Autowired
    UserAuthenticationService userAuthenticationService;
    @Autowired
    WalletPasswordRequestRepository walletPasswordRequestRepository;
    @Autowired
    WalletPasswordService walletPasswordService;

    @PostMapping("/user/share-password/{id}")
    public RedirectView sharePasswordHandler(@RequestParam String email, @PathVariable(value="id") int id) {
        User userTo = userRepository.findByEmail(email);

        if(userTo == null)
            return new RedirectView("/user/view-passwords");

        User userFrom = userAuthenticationService.getCurrentUser();
        WalletPassword walletPassword = walletPasswordRepository.findById(id).get();
        walletPasswordRequestRepository.save(new WalletPasswordRequest(userFrom, userTo, walletPassword));

        return new RedirectView("/user/view-passwords");
    }

    @GetMapping("/accept-password-request/{id}")
    public RedirectView acceptPasswordShareRequestHandler( @PathVariable(value="id") int id) {
        User userFrom = userRepository.findById(id).get();
        User currUser = userAuthenticationService.getCurrentUser();
        List<WalletPasswordRequest> passwordRequests = walletPasswordRequestRepository.findAllByUserFromAndUserTo(userFrom, currUser);

        List<WalletPassword> walletPasswordList = new ArrayList<>();
        passwordRequests
                .stream()
                .forEach(request -> {
                    WalletPassword walletPassword = walletPasswordService
                            .generateUpdatedWalletPassword(request.getWalletPassword(), currUser, userFrom);
                    walletPasswordList.add(walletPassword);
                });

        walletPasswordRequestRepository.deleteAll(passwordRequests);
        walletPasswordRepository.saveAll(walletPasswordList);
        return new RedirectView("/user/view-passwords");
    }

}
