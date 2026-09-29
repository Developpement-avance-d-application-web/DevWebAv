package com.spring.henallux.firstspringproject.controller;

import com.spring.henallux.firstspringproject.model.Category;
import com.spring.henallux.firstspringproject.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogController {
    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/catalogue")
    public String showCatalog(@RequestParam(name = "categoryId", required = false) String categoryId, Model model) {
        model.addAttribute("title", "Catalogue — Église de l’Empire");
        model.addAttribute("categories", catalogService.getCategories());
        if (categoryId == null) {
            model.addAttribute("products", catalogService.getProducts());
        } else {
            Category category = catalogService.findCategoryById(categoryId);
            if (category == null) {
                model.addAttribute("categoryError", "Cette catégorie n’existe pas. Choisis une catégorie ci-dessous.");
            } else {
                model.addAttribute("selectedCategory", category);
                model.addAttribute("products", catalogService.getProductsByCategory(categoryId));
            }
        }
        return "catalogue";
    }
}
