package com.spring.henallux.firstspringproject.model;

// Données saisies dans le formulaire de recherche, pas un produit du catalogue.
// Un objet Java contenant la référence saisie pour chercher un article.
public class ProductLookupForm {
    private String reference;

    public ProductLookupForm() {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
