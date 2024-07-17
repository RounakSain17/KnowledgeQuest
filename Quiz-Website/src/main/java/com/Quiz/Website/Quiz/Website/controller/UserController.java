package com.Quiz.Website.Quiz.Website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Quiz.Website.Quiz.Website.model.User;
import com.Quiz.Website.Quiz.Website.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
    try {
        userService.register(user);
        return "redirect:/user/login";
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/user/register";
    }
}

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "user/login";
    }

    
    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
    User loggedInUser = userService.login(user.getUsername(), user.getPassword());
    if (loggedInUser != null) {
        model.addAttribute("username", loggedInUser.getUsername());
        return "redirect:/quiz/select-subject";
    } else {
        redirectAttributes.addFlashAttribute("error", "Oops! Credentials do not match.");
        return "redirect:/user/login";
    }
}
    

}
