package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.stream.Collectors;


@Controller
public class UserController {

    @GetMapping("/user")
    public String userPage(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {
            String roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(", "));
            model.addAttribute("currentUser", user);
        } else {
            return "redirect:/login";
        }
        return "users/user";
    }



}


