package pl.application.passwordwallet.configuration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static pl.application.passwordwallet.service.MyUserDetailsService.constants;

@Component
public class SHA512PasswordEncoder implements PasswordEncoder {

    private String identifier = "{sha512}";
    @Override
    public String encode(CharSequence rawPassword) {
        String text = rawPassword.toString();
        try {
            //get an instance of SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            //calculate message digest of the input string  - returns byte array
            byte[] messageDigest = md.digest(text.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return identifier+hashtext;
        }

        // If wrong message digest algorithm was specified
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String salt = constants.getSalt();
        return encode(rawPassword.toString() + salt).equals("{sha512}" + encodedPassword);
    }
}
