package com.mehdi.ws_projektarbete_1.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Returnerar filen login.html från templates-mappen
    }
    @PostMapping("/v1/authenticate")
    public String authenticateUser(@RequestParam String username, @RequestParam String password) {
        // Här kan du validera användarens inloggningsuppgifter
        if ("admin".equals(username) && "admin123".equals(password)) {
            return "redirect:/v1/weather"; // Skickar användaren till väderinformationen
        } else {
            return "login"; // Om misslyckas, visa login-sidan igen
        }
    }
}

