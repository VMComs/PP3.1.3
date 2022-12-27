package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.*;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<User> findAll () {
        return userRepository.findAll();
    }

    public User findUser(int id) {
        Optional<User> foundUser = userRepository.findById(id);
//        foundUser.get().getRoles().forEach(System.out::println);
        return foundUser.orElse(null);
    }

    @Transactional
    public void saveUser(User user) {
        List<Role> roles = new ArrayList<>();
        if(user.getRoles()==null) {
            Role newRole = new Role("ROLE_NONE");
            roles.add(newRole);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }


    @Transactional
    public void updateUser(int id, User updatedUser) {
        User foundUser = userRepository.findById(id).get();
        updatedUser.setId(id);
        updatedUser.setRoles(foundUser.getRoles());
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        userRepository.save(updatedUser);
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userFound = userRepository.findUserByName(username);
        if(userFound.isEmpty()){
            throw new UsernameNotFoundException(String.format("User '%s' not found!", username));
        }
        return userFound.get();
    }

    public User getUser(String username) {
        return userRepository.findUserByName(username).get();
    }


}
