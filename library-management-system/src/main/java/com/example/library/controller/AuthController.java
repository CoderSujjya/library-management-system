package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                         @RequestParam String password,
                         HttpSession session,
                         Model model) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
        session.setAttribute("loggedInUser", user.getUsername());
        return "redirect:/welcome";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String email,
                            @RequestParam String fullName,
                            Model model) {
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Username already taken.");
            return "register";
        }
        User user = new User(username, passwordEncoder.encode(password), email, fullName);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
