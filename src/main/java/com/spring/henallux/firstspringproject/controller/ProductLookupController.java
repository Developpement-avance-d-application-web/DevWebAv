package com.spring.henallux.firstspringproject.controller;

import com.spring.henallux.firstspringproject.model.Product;
import com.spring.henallux.firstspringproject.model.ProductLookupForm;
import com.spring.henallux.firstspringproject.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recherche-reference")
public class ProductLookupController {
    private final CatalogService catalogService;

    @Autowired
    public ProductLookupController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public String showSearchForm(Model donneesPage) {
        // Clé utilisée par th:object dans productLookup.html.
        donneesPage.addAttribute("productLookupForm", new ProductLookupForm());
        prepareSearchPage(donneesPage);
        return "productLookup";
    }

    @PostMapping
    public String searchProduct(
            @ModelAttribute("productLookupForm") ProductLookupForm formulaireDeRechercheDArticle,
            BindingResult errors,
            Model model) {
        // Spring a rempli form.reference avec le champ "reference" du POST.
        String reference = formulaireDeRechercheDArticle.getReference();
        Product product = null;
        if (reference == null || reference.isBlank()) {
            errors.rejectValue("reference", "product.reference.required",
                    "Saisis une référence d'article.");
        } else if (reference.length() > 30) {
            errors.rejectValue("reference", "product.reference.tooLong",
                    "La référence ne peut pas dépasser 30 caractères.");
        } else {
            product = catalogService.findByReference(reference);
            if (product == null) {
                errors.rejectValue("reference", "product.reference.unknown",
                        "Aucun article ne correspond à cette référence.");
            }
        }

        if (errors.hasErrors()) {
            // Conserver le formulaire rempli et ses erreurs dans le Model.
            prepareSearchPage(model);
            return "productLookup";
        }

        // Nouvelle requête GET : ProductController préparera la fiche produit.
        return "redirect:/produits/" + product.getId();
    }

    private void prepareSearchPage(Model model) {
        model.addAttribute("title", "Rechercher un article — Église de l'Empire");
        model.addAttribute("products", catalogService.getProducts());
    }
}
