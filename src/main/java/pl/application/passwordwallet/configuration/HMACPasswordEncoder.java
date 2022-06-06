package pl.application.passwordwallet.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA512;
import static pl.application.passwordwallet.service.MyUserDetailsService.constants;

@Component
public class HMACPasswordEncoder implements PasswordEncoder {
    private String key = "SecretKey";
    private String identifier = "{hmac_sha512}";

    @Override
    public String encode(CharSequence rawPassword) {
        String text = rawPassword.toString();
        Mac sha512Hmac;
        String result="";
        try {
            final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA512");
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(text.getBytes(StandardCharsets.UTF_8));
            result = Base64.getEncoder().encodeToString(macData);

        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
        }
        return identifier+result;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String salt = constants.getSalt();
        return encode(rawPassword.toString() + salt).equals("{hmac_sha512}" + encodedPassword);
    }
}
