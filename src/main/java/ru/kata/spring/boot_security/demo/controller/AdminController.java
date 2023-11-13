package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getUsers(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        model.addAttribute("currentUser", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("roleAdmin", null);
        model.addAttribute("roleUser", null);
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("users", userService.getUsers());
        return "admin";
    }

    @PostMapping
    public String add(@ModelAttribute("newUser") User user,
                      @ModelAttribute("roleAdmin") String roleAdmin,
                      @ModelAttribute("roleUser") String roleUser
    ) {
        Set<Role> listRoles = new HashSet<>();
        if (!roleAdmin.isEmpty()) {
            listRoles.add(roleService.getById(Long.parseLong(roleAdmin)));
        }
        if (!roleUser.isEmpty()) {
            listRoles.add(roleService.getById(Long.parseLong(roleUser)));
        }
        userService.addUser(user);
        return "redirect:/admin";
    }
    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") User user,
                           @ModelAttribute("roleAdmin") String roleAdmin,
                           @ModelAttribute("roleUser") String roleUser) {
        Set<Role> listRoles2 = new HashSet<>();
        if (!roleAdmin.isEmpty()) {
            listRoles2.add(roleService.getById(Long.parseLong(roleAdmin)));
        }
        if (!roleUser.isEmpty()) {
            listRoles2.add(roleService.getById(Long.parseLong(roleUser)));
        }
        user.setRoles(listRoles2);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") Long id) {
        userService.removeById(id);
        return "redirect:/admin";
    }
}
