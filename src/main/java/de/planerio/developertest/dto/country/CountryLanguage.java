package de.planerio.developertest.dto.country;

public enum CountryLanguage {
    DE("de"),
    FR("fr"),
    EN("en"),
    ES("es"),
    IT("it");

    private String displayText;

    public String getDisplayText() {
        return displayText;
    }

    CountryLanguage(final String displayText) {
        this.displayText = displayText;
    }

    @Override
    public String toString() {
        return displayText;
    }
}
