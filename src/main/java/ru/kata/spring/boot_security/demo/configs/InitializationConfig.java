package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Configuration
public class InitializationConfig {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public InitializationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void Initialization() {
        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");
        User MrAdmin = new User(Long.valueOf(1), "MrAdmin", "123", "Petr I", 34, new HashSet<>() {{
            add(admin);
        }});
        User MrUser = new User(Long.valueOf(2), "MrUser", "123", "Nikolay II", 54, new HashSet<>() {{
            add(user);
        }});
        MrAdmin.setPassword(passwordEncoder.encode(MrAdmin.getPassword()));
        MrUser.setPassword(passwordEncoder.encode(MrUser.getPassword()));
        userRepository.save(MrAdmin);
        userRepository.save(MrUser);
    }
}
