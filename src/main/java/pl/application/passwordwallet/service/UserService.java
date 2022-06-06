package pl.application.passwordwallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.application.passwordwallet.configuration.HMACPasswordEncoder;
import pl.application.passwordwallet.configuration.SHA512PasswordEncoder;
import pl.application.passwordwallet.model.Password;
import pl.application.passwordwallet.model.Role;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.repository.RoleRepository;
import pl.application.passwordwallet.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordService passwordService;
    @Autowired
    RoleRepository roleRepository;

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        Password password = passwordService.getEncryptedPassword(user.getChosenPassword(), user.getChosenEncodingMethod());
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        user.setPassword(password);
        user.setActive(true);
        return userRepository.save(user);

    }

}
