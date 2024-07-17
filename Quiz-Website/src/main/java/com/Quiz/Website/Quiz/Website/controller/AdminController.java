package com.Quiz.Website.Quiz.Website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Quiz.Website.Quiz.Website.model.Admin;
import com.Quiz.Website.Quiz.Website.model.Question;
import com.Quiz.Website.Quiz.Website.service.AdminService;
import com.Quiz.Website.Quiz.Website.service.QuestionService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private QuestionService questionService;

     @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Admin admin) {
        adminService.register(admin);
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Admin admin, Model model) {
        Admin loggedInAdmin = adminService.login(admin.getUsername(), admin.getPassword());
        if (loggedInAdmin != null) {
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "admin/dashboard";
    }
    @GetMapping("/add-question")
    public String showAddQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        return "admin/add-question";
    }

    @PostMapping("/add-question")
    public String addQuestion(@ModelAttribute Question question) {
        questionService.saveQuestion(question);
        return "redirect:/admin/dashboard";
    }
}
