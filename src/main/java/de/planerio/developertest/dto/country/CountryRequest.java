package de.planerio.developertest.dto.country;

import javax.validation.constraints.NotBlank;

public class CountryRequest {

    @NotBlank(message = "`name` is required")
    private String name;

    @NotBlank(message = "`language` is required")
    private String language;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }
}
