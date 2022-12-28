package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DefaultUsersAdd {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUsersAdd(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Transactional
    public void defaultUsers () {
        Set<Role> rolesAdmin = new HashSet<>();
        Role adminRole = new Role("ROLE_ADMIN");
        User adminUser = new User("admin", passwordEncoder.encode("admin"));

        rolesAdmin.add(adminRole);
        adminUser.setRoles(rolesAdmin);
        userRepository.save(adminUser);

        Set<Role> rolesUser = new HashSet<>();
        Role userRole = new Role("ROLE_USER");
        User userUser = new User("user", passwordEncoder.encode("user"));

        rolesUser.add(userRole);
        userUser.setRoles(rolesUser);
        userRepository.save(userUser);

    }

}
