package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.RoleService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final RoleService roleService;

    @Autowired
    public AdminController(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    // Только показ главной страницы админки
    @GetMapping({"", "/"})
    public String showAdminPanel(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("currentUser", adminService.getCurrentUser());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/admin";
    }
}