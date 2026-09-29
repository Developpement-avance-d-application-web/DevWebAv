package com.spring.henallux.firstspringproject.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductLookupForm {
    @NotBlank(message = "Saisis une référence d’article.")
    @Size(max = 30, message = "La référence ne peut pas dépasser 30 caractères.")
    private String reference;

    public ProductLookupForm() {
    }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

}
