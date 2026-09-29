package com.spring.henallux.firstspringproject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.docker.compose.enabled=false")
@AutoConfigureMockMvc
class Lab3WebTests {
    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @ValueSource(strings = {"/", "/hello", "/societe", "/catalogue", "/inscription", "/recherche-reference"})
    void publicPagesRenderWithNavigation(String url) throws Exception {
        mvc.perform(get(url)).andExpect(status().isOk())
                .andExpect(content().string(containsString("/catalogue")))
                .andExpect(content().string(containsString("/inscription")));
    }

    @Test
    void catalogShowsOnlyTheSelectedCategory() throws Exception {
        mvc.perform(get("/catalogue")).andExpect(model().attribute("categories", hasSize(3)));
        mvc.perform(get("/catalogue").param("categoryId", "objets"))
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(content().string(containsString("Calice impérial")))
                .andExpect(model().attribute("products", everyItem(hasProperty("categoryId", is("objets")))));
        mvc.perform(get("/catalogue").param("categoryId", "textiles"))
                .andExpect(content().string(containsString("Corporal brodé")))
                .andExpect(content().string(containsString("Étole impériale")));
        mvc.perform(get("/catalogue").param("categoryId", "unknown"))
                .andExpect(status().isOk()).andExpect(model().attributeExists("categoryError"));
    }

    @Test
    void searchRedirectsToAnIndependentProductPage() throws Exception {
        mvc.perform(post("/recherche-reference").with(csrf()).param("reference", " cal-001 "))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/produits/1"));
        mvc.perform(get("/produits/1")).andExpect(status().isOk())
                .andExpect(model().attributeExists("product", "addToCartForm"))
                .andExpect(content().string(containsString("320,00 €")));
        mvc.perform(get("/produits/999999")).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "UNKNOWN", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"})
    void searchRejectsInvalidReferences(String reference) throws Exception {
        mvc.perform(post("/recherche-reference").with(csrf()).param("reference", reference))
                .andExpect(status().isOk()).andExpect(view().name("productLookup"))
                .andExpect(model().attributeHasFieldErrors("productLookupForm", "reference"))
                .andExpect(model().attributeExists("title", "products"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "99"})
    void quantityPreviewAcceptsValidBoundaries(String quantity) throws Exception {
        mvc.perform(post("/produits/1/ajout").with(csrf()).param("quantity", quantity)
                        .param("unitPrice", "0.01"))
                .andExpect(status().isOk()).andExpect(view().name("cartPreview"))
                .andExpect(model().attribute("quantity", Integer.valueOf(quantity)))
                .andExpect(model().attribute("product", hasProperty("unitPrice", comparesEqualTo(new java.math.BigDecimal("320")))))
                .andExpect(content().string(containsString("Aucun panier")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "0", "-1", "100", "abc", "1.5", "9999999999999999999"})
    void quantityErrorsKeepTheProductAndForm(String quantity) throws Exception {
        mvc.perform(post("/produits/1/ajout").with(csrf()).param("quantity", quantity))
                .andExpect(status().isOk()).andExpect(view().name("productDetail"))
                .andExpect(model().attributeHasFieldErrors("addToCartForm", "quantity"))
                .andExpect(model().attributeExists("product", "title"));
    }

    @Test
    void missingQuantityAndUnknownProductAreHandled() throws Exception {
        mvc.perform(post("/produits/1/ajout").with(csrf()))
                .andExpect(model().attributeHasFieldErrors("addToCartForm", "quantity"));
        mvc.perform(post("/produits/99999/ajout").with(csrf()).param("quantity", "2"))
                .andExpect(status().isNotFound());
    }

    private MultiValueMap<String, String> validRegistration() {
        MultiValueMap<String, String> fields = new LinkedMultiValueMap<>();
        fields.add("firstName", "Leia");
        fields.add("lastName", "Organa");
        fields.add("login", "leia.organa");
        fields.add("email", "leia@example.org");
        fields.add("phone", "+32 (0) 471 12 34 56");
        fields.add("addressLine1", "10 rue de la République");
        fields.add("postalCode", "05000");
        fields.add("city", "Namur");
        fields.add("countryCode", "BE");
        fields.add("password", "Test-password-123");
        fields.add("passwordConfirmation", "Test-password-123");
        return fields;
    }

    @Test
    void validRegistrationAllowsOmittedOptionalFieldsAndDoesNotCreateAnAccount() throws Exception {
        mvc.perform(post("/inscription").with(csrf()).params(validRegistration()))
                .andExpect(status().isOk()).andExpect(view().name("registrationPreview"))
                .andExpect(content().string(containsString("Leia")))
                .andExpect(content().string(containsString("Aucun compte")))
                .andExpect(content().string(not(containsString("Test-password-123"))));
    }

    @Test
    void emptyRegistrationShowsAllRequiredErrorsAndReloadsCountries() throws Exception {
        mvc.perform(post("/inscription").with(csrf()))
                .andExpect(status().isOk()).andExpect(view().name("registration"))
                .andExpect(model().attributeHasFieldErrors("registrationForm", "firstName", "lastName", "login",
                        "email", "phone", "addressLine1", "postalCode", "city", "countryCode", "password", "passwordConfirmation"))
                .andExpect(model().attribute("countries", hasSize(4)))
                .andExpect(content().string(containsString("Belgique")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"firstName", "lastName", "login", "email", "phone", "addressLine1", "postalCode", "city",
            "countryCode", "password", "passwordConfirmation", "addressLine2", "organizationName", "newsletterOptIn"})
    void invalidFieldsAreRejectedWithoutExposingPasswords(String field) throws Exception {
        var fields = validRegistration();
        String invalidValue = switch (field) {
            case "email" -> "not-an-email";
            case "countryCode" -> "ZZ";
            case "passwordConfirmation" -> "Different-password-456";
            case "addressLine2" -> "A".repeat(121);
            case "organizationName" -> "A".repeat(101);
            case "phone" -> "abcdefghi";
            case "newsletterOptIn" -> "nonsense";
            default -> "x";
        };
        fields.set(field, invalidValue);
        mvc.perform(post("/inscription").with(csrf()).params(fields))
                .andExpect(status().isOk()).andExpect(view().name("registration"))
                .andExpect(model().attributeHasFieldErrors("registrationForm", field))
                .andExpect(model().attribute("countries", hasSize(4)))
                .andExpect(content().string(containsString("value=\"leia.organa\"".replace("leia.organa", field.equals("login") ? "x" : "leia.organa"))))
                .andExpect(content().string(not(matchesPattern("(?s).*<input(?=[^>]*type=\"password\")(?=[^>]*value=)[^>]*>.*"))))
                .andExpect(content().string(not(containsString("Test-password-123"))))
                .andExpect(content().string(not(containsString("Different-password-456"))));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/recherche-reference", "/inscription", "/produits/1/ajout"})
    void csrfProtectionIsNotDisabled(String url) throws Exception {
        mvc.perform(post(url)).andExpect(status().isForbidden());
    }
}
