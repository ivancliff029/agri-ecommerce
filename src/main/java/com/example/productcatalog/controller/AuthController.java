package com.example.productcatalog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Make sure you have a login.html in the templates folder
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // Make sure you have a register.html in the templates folder
    }
}
