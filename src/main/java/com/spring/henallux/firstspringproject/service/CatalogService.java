package com.spring.henallux.firstspringproject.service;

import com.spring.henallux.firstspringproject.model.Category;
import com.spring.henallux.firstspringproject.model.Product;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class CatalogService {
    // Données de démonstration : remplacées par un DAO au labo Hibernate.
    private final List<Category> categories = List.of(
            new Category("objets", "Objets liturgiques"),
            new Category("encens", "Encens et accessoires"),
            new Category("textiles", "Textiles liturgiques"));

    private final List<Product> products = List.of(
            new Product(1L, "CAL-001", "Calice impérial", "Calice en acier brossé et émail noir.", new BigDecimal("320.00"), "objets"),
            new Product(2L, "PAT-001", "Patène impériale", "Patène en acier inoxydable ornée de l’emblème impérial.", new BigDecimal("120.00"), "objets"),
            new Product(3L, "ENC-001", "Encensoir de la Flotte", "Encensoir en laiton vieilli pour les cérémonies de la Flotte.", new BigDecimal("280.00"), "encens"),
            new Product(4L, "NAV-001", "Navette à encens", "Navette en bronze avec sa cuillère pour préparer l’encens.", new BigDecimal("85.00"), "encens"),
            new Product(5L, "COR-001", "Corporal brodé", "Linge d’autel en lin brodé au fil argenté.", new BigDecimal("38.00"), "textiles"),
            new Product(6L, "ETO-001", "Étole impériale", "Étole sombre brodée pour les célébrations impériales.", new BigDecimal("95.00"), "textiles"));

    public List<Category> getCategories() { return categories; }
    public List<Product> getProducts() { return products; }

    public Category findCategoryById(String id) {
        for (Category category : categories) {
            if (category.getId().equals(id)) return category;
        }
        return null;
    }

    public List<Product> getProductsByCategory(String categoryId) {
        return products.stream().filter(product -> product.getCategoryId().equals(categoryId)).toList();
    }

    public Product findByReference(String reference) {
        if (reference == null || reference.isBlank()) return null;
        String normalizedReference = reference.strip().toUpperCase(Locale.ROOT);
        for (Product product : products) {
            if (product.getReference().equals(normalizedReference)) return product;
        }
        return null;
    }

    public Product findById(Long id) {
        for (Product product : products) {
            if (product.getId().equals(id)) return product;
        }
        return null;
    }
}
