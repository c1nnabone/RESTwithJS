package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;

@SpringBootApplication
public class SpringBootSecurityDemoApplication implements CommandLineRunner {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public SpringBootSecurityDemoApplication(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired


    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
    }


    @Override
    public void run(String... args) {
        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");
        User MrAdmin = new User(Long.valueOf(1), "MrAdmin", "123", "Petr I", 34);
        User MrUser = new User(Long.valueOf(2), "MrUser", "123", "Nikolay II", 54);
        MrAdmin.setPassword(passwordEncoder.encode(MrAdmin.getPassword()));
        MrUser.setPassword(passwordEncoder.encode(MrUser.getPassword()));
        MrAdmin.setRoles(new HashSet<>() {{
            add(admin);
        }});
        MrUser.setRoles(new HashSet<>() {{
            add(user);
        }});
        userRepository.save(MrAdmin);
        userRepository.save(MrUser);
    }
}
