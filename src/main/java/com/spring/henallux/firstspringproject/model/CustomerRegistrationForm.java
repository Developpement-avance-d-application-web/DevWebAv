package com.spring.henallux.firstspringproject.model;

import jakarta.validation.constraints.*;

public class CustomerRegistrationForm {
    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 2, max = 50, message = "Entre 2 et 50 caractères attendus.")
    private String firstName;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 2, max = 50, message = "Entre 2 et 50 caractères attendus.")
    private String lastName;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 4, max = 30, message = "Entre 4 et 30 caractères attendus.")
    private String login;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 0, max = 254, message = "Entre 0 et 254 caractères attendus.")
    @Email(message = "Indique une adresse email valide.")
    private String email;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 6, max = 25, message = "Entre 6 et 25 caractères attendus.")
    @Pattern(regexp = "[+0-9(). /-]+", message = "Utilise des chiffres et les séparateurs usuels pour le téléphone.")
    private String phone;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 5, max = 120, message = "Entre 5 et 120 caractères attendus.")
    private String addressLine1;

    @Size(min = 0, max = 120, message = "Entre 0 et 120 caractères attendus.")
    private String addressLine2;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 2, max = 12, message = "Entre 2 et 12 caractères attendus.")
    private String postalCode;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 2, max = 80, message = "Entre 2 et 80 caractères attendus.")
    private String city;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 0, max = 2, message = "Entre 0 et 2 caractères attendus.")
    private String countryCode;

    @Size(min = 0, max = 100, message = "Entre 0 et 100 caractères attendus.")
    private String organizationName;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 12, max = 128, message = "Entre 12 et 128 caractères attendus.")
    private String password;

    @NotBlank(message = "Ce champ est obligatoire.")
    @Size(min = 12, max = 128, message = "Entre 12 et 128 caractères attendus.")
    private String passwordConfirmation;

    private Boolean newsletterOptIn = false;

    public CustomerRegistrationForm() {
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPasswordConfirmation() { return passwordConfirmation; }
    public void setPasswordConfirmation(String passwordConfirmation) { this.passwordConfirmation = passwordConfirmation; }

    public Boolean getNewsletterOptIn() { return newsletterOptIn; }
    public void setNewsletterOptIn(Boolean newsletterOptIn) { this.newsletterOptIn = newsletterOptIn; }

}
