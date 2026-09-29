package com.spring.henallux.firstspringproject.controller;

import com.spring.henallux.firstspringproject.model.Product;
import com.spring.henallux.firstspringproject.model.ProductLookupForm;
import com.spring.henallux.firstspringproject.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;
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
            @Valid @ModelAttribute("productLookupForm") ProductLookupForm formulaireDeRechercheDArticle,
            BindingResult errors,
            Model model) {
        // Spring a rempli et validé la référence avant cet appel.
        Product product = null;
        if (!errors.hasErrors()) {
            String reference = formulaireDeRechercheDArticle.getReference();
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
