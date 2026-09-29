package com.spring.henallux.firstspringproject.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CatalogServiceTests {
    private final CatalogService catalog = new CatalogService();

    @Test
    void referenceSearchIgnoresCaseAndOuterSpaces() {
        var product = catalog.findByReference("  cal-001  ");
        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals(new BigDecimal("320.00"), product.getUnitPrice());
    }

    @Test
    void missingOrUnknownReferenceDoesNotMatchAProduct() {
        assertNull(catalog.findByReference(null));
        assertNull(catalog.findByReference("   "));
        assertNull(catalog.findByReference("spring"));
        assertNull(catalog.findByReference("UNKNOWN"));
    }

    @Test
    void idSearchReturnsOnlyExistingProducts() {
        assertEquals("PAT-001", catalog.findById(2L).getReference());
        assertNull(catalog.findById(999L));
        assertNull(catalog.findById(null));
    }
}
