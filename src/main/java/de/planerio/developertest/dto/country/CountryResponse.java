package de.planerio.developertest.dto.country;

public class CountryResponse {
    private final long id;
    private final String name;
    private final String language;

    public CountryResponse(final long id,
                           final String name,
                           final String language) {
        this.id = id;
        this.name = name;
        this.language = language;
    }

    public static CountryDTOBuilder builder () {
        return new CountryDTOBuilder();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public static final class CountryDTOBuilder {
        private long id;
        private String name;
        private String language;

        public CountryDTOBuilder id(final long id) {
            this.id = id;
            return this;
        }

        public CountryDTOBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public CountryDTOBuilder language(final String language) {
            this.language = language;
            return this;
        }

        public CountryResponse build() {
            return new CountryResponse(
                    id,
                    name,
                    language
            );
        }
    }
}
