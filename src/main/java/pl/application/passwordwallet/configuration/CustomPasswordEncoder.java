package pl.application.passwordwallet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class CustomPasswordEncoder {
    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("sha512", new SHA512PasswordEncoder());
        encoders.put("hmac_sha512", new HMACPasswordEncoder());
        return new DelegatingPasswordEncoder("sha512", encoders);
    }
}
