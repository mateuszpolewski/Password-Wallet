package pl.application.passwordwallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.application.passwordwallet.configuration.AESenc;
import pl.application.passwordwallet.model.Password;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.model.WalletPassword;
import pl.application.passwordwallet.repository.WalletPasswordRepository;

import java.util.List;

@Service
public class WalletPasswordService {
    @Autowired
    WalletPasswordRepository walletPasswordRepository;
    @Autowired
    UserAuthenticationService userAuthenticationService;

    public WalletPassword saveWalletPassword(WalletPassword walletPassword) throws Exception {
        User currentUser = userAuthenticationService.getCurrentUser();
        String userMasterPassword = currentUser.getPassword().getPassword();
        walletPassword.setHashedPassword(
                AESenc.encrypt(
                        walletPassword.getHashedPassword(),
                        AESenc.generateKey(userMasterPassword)
                ));
        walletPassword.setUser(currentUser);
        return walletPasswordRepository.save(walletPassword);
    }

    public List<WalletPassword> findCurrentUserWalletPasswords() {
        int currentUserId = userAuthenticationService.getCurrentUser().getId();
        return walletPasswordRepository.findByUserId(currentUserId);
    }

    public List<WalletPassword> updateWalletPasswords(String encryptedOldMasterPassword, String encryptedNewMasterPassword) {
        List<WalletPassword> walletPasswords = findCurrentUserWalletPasswords();
        walletPasswords.forEach(walletPassword -> {
            try {
                String rawWalletPassword = AESenc.decrypt(
                        walletPassword.getHashedPassword(),
                        AESenc.generateKey(encryptedOldMasterPassword)
                );
                String encryptedWithNewMasterPassword = AESenc.encrypt(
                        rawWalletPassword,
                        AESenc.generateKey(encryptedNewMasterPassword)
                );
                walletPassword.setHashedPassword(encryptedWithNewMasterPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return walletPasswordRepository.saveAll(walletPasswords);
    }
    public WalletPassword generateUpdatedWalletPassword(WalletPassword walletPassword, User newOwner ,User sharedBy) {
        return new WalletPassword(walletPassword.getHashedPassword(),
                walletPassword.getTitle(),
                walletPassword.getLink(),
                walletPassword.getDescription(),
                newOwner,
                sharedBy);
    }
}

