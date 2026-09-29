package com.spring.henallux.firstspringproject.controller;

import com.spring.henallux.firstspringproject.model.CustomerRegistrationForm;
import com.spring.henallux.firstspringproject.service.CountryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inscription")
public class RegistrationController {
    private final CountryService countryService;

    @Autowired
    public RegistrationController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public String showRegistration(Model model) {
        model.addAttribute("registrationForm", new CustomerRegistrationForm());
        prepareRegistrationPage(model);
        return "registration";
    }

    @PostMapping
    public String validateRegistration(
            @Valid @ModelAttribute("registrationForm") CustomerRegistrationForm form,
            BindingResult errors, Model model) {
        if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("passwordConfirmation")
                && !form.getPassword().equals(form.getPasswordConfirmation())) {
            errors.rejectValue("passwordConfirmation", "registration.password.mismatch",
                    "Les deux mots de passe doivent être identiques.");
        }
        if (!errors.hasFieldErrors("countryCode") && !countryService.exists(form.getCountryCode())) {
            errors.rejectValue("countryCode", "registration.country.unknown",
                    "Choisis un pays de livraison proposé dans la liste.");
        }
        if (form.getNewsletterOptIn() == null) form.setNewsletterOptIn(false);

        // Aucune conservation des mots de passe ; la persistance viendra plus tard.
        form.setPassword(null);
        form.setPasswordConfirmation(null);
        if (errors.hasErrors()) {
            prepareRegistrationPage(model);
            return "registration";
        }
        model.addAttribute("firstName", form.getFirstName());
        model.addAttribute("lastName", form.getLastName());
        model.addAttribute("title", "Formulaire validé — Église de l’Empire");
        model.asMap().remove("registrationForm");
        model.asMap().remove(BindingResult.MODEL_KEY_PREFIX + "registrationForm");
        return "registrationPreview";
    }

    private void prepareRegistrationPage(Model model) {
        model.addAttribute("title", "Inscription — Église de l’Empire");
        model.addAttribute("countries", countryService.getCountries());
    }
}
