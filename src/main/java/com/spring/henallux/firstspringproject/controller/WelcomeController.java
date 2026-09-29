package com.spring.henallux.firstspringproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    @GetMapping({"/", "/hello"})
    public String home(Model model) {
        model.addAttribute("title", "Église de l'Empire");
        // Nom du template à rendre, pas le texte envoyé au navigateur.
        return "welcome";
    }
}
