package com.spring.henallux.firstspringproject.controller;

import com.spring.henallux.firstspringproject.model.Product;
import com.spring.henallux.firstspringproject.model.AddToCartForm;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.spring.henallux.firstspringproject.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/produits")
public class ProductController {
    private final CatalogService catalogService;

    @Autowired
    public ProductController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") Long id, Model model) {
        prepareProductPage(id, model);
        AddToCartForm form = new AddToCartForm();
        form.setQuantity(1);
        model.addAttribute("addToCartForm", form);
        return "productDetail";
    }

    @PostMapping("/{id}/ajout")
    public String previewAddition(@PathVariable("id") Long id,
            @Valid @ModelAttribute("addToCartForm") AddToCartForm form,
            BindingResult errors, Model model) {
        // Le prix et l'article viennent du catalogue, jamais des champs HTTP.
        prepareProductPage(id, model);
        if (errors.hasErrors()) return "productDetail";
        model.addAttribute("title", "Prévisualisation de l’ajout — Église de l’Empire");
        model.addAttribute("quantity", form.getQuantity());
        return "cartPreview";
    }

    private void prepareProductPage(Long id, Model model) {
        Product product = catalogService.findById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit introuvable");
        }
        model.addAttribute("title", product.getName() + " — Église de l’Empire");
        model.addAttribute("product", product);
    }
}
