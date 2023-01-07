package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


@Controller
public class UserController {


    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/admin")
    public String index(Model model) {
        model.addAttribute("users", userServiceImpl.findAll());
        return "show-all";
    }

    @GetMapping("/user")
    public String indexUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("foundIdUser", userServiceImpl.getUser(authentication.getName()));
        return "/user-info";
    }


    @GetMapping("/admin/users/{id}")
    public String showUser(@PathVariable("id") int id, Model model) {
        User ourUser = userServiceImpl.findUser(id);
        model.addAttribute("foundIdUser", ourUser);
        return "user-info";
    }

    @GetMapping("/admin/new")
    public String newUser(@ModelAttribute("newUser") User user) {
        return "create";
    }

    @PostMapping("/admin")
    public String create(@ModelAttribute("newUser") @Valid User user, @RequestParam("listRoles[]") String[] listRoles, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "create";
        }

        userServiceImpl.saveUser(user, listRoles);
        return "redirect:/admin";
    }



    @GetMapping("/admin/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("userUpdate", userServiceImpl.findUser(id));
        System.out.println(userServiceImpl.findUser(id));
        return "edit";
    }

    @PatchMapping("/admin/{id}")
    public String update(@ModelAttribute("userUpdate") @Valid User user, BindingResult bindingResult, @PathVariable("id") int id) {
        System.out.println(user);

        if(bindingResult.hasErrors()) {
            return "edit";
        }

        userServiceImpl.updateUser(id, user);
        return "redirect:/admin";
    }



    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") int id) {
        userServiceImpl.delete(id);
        return "redirect:/admin";
    }
}
