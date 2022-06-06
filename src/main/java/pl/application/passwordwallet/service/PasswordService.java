package pl.application.passwordwallet.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pl.application.passwordwallet.configuration.HMACPasswordEncoder;
import pl.application.passwordwallet.configuration.SHA512PasswordEncoder;
import pl.application.passwordwallet.model.Password;
import pl.application.passwordwallet.repository.PasswordRepository;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

@Service
public class PasswordService {

    @Autowired
    PasswordRepository passwordRepository;
    @Autowired
    SHA512PasswordEncoder sha512PasswordEncoder;
    @Autowired
    HMACPasswordEncoder hmacPasswordEncoder;

    public Password getById(int id) {return (Password) passwordRepository.findById(id); }

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return DatatypeConverter.printBase64Binary(salt);
    }

    public Password getEncryptedPassword(String rawPassword, String encodingMethod) {
        Password password = new Password();
        password.setSalt(generateSalt());
        String fullPassword = rawPassword + password.getSalt();
        if(encodingMethod.equals("SHA512")) {
            password.setPassword(sha512PasswordEncoder.encode(fullPassword));
        }
        if(encodingMethod.equals("HMAC")) {
            password.setPassword(hmacPasswordEncoder.encode(fullPassword));
        }
        password.setEncodingMethod(encodingMethod);
        return password;
    }

    public void updateUserPassword(Password password, String rawPassword, String encodingMethod) {
        Password newPassword = getEncryptedPassword(rawPassword, encodingMethod);
        password.setEncodingMethod(encodingMethod);
        password.setPassword(newPassword.getPassword());
        password.setSalt(newPassword.getSalt());
        passwordRepository.save(password);
    }
}
