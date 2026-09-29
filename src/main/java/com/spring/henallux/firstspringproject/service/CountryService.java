package com.spring.henallux.firstspringproject.service;

import com.spring.henallux.firstspringproject.model.Country;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CountryService {
    private final List<Country> countries = List.of(
            new Country("BE", "Belgique"), new Country("FR", "France"),
            new Country("LU", "Luxembourg"), new Country("DE", "Allemagne"));

    public List<Country> getCountries() { return countries; }

    public boolean exists(String code) {
        return countries.stream().anyMatch(country -> country.getCode().equals(code));
    }
}
