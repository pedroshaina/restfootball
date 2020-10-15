package de.planerio.developertest.dto.league;

public class LeagueResponse {
    private final long id;
    private final String name;
    private final long countryId;

    public LeagueResponse(final long id, final String name, final long countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCountryId() {
        return countryId;
    }

    public static LeagueResponseBuilder builder() {
        return new LeagueResponseBuilder();
    }

    public static final class LeagueResponseBuilder {
        private long id;
        private String name;
        private long countryId;

        public LeagueResponseBuilder id(final long id) {
            this.id = id;
            return this;
        }

        public LeagueResponseBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public LeagueResponseBuilder countryId(final long countryId) {
            this.countryId = countryId;
            return this;
        }

        public LeagueResponse build() {
            return new LeagueResponse(
                    this.id,
                    this.name,
                    this.countryId
            );
        }
    }
}
