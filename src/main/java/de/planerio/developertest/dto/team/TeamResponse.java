package de.planerio.developertest.dto.team;

public class TeamResponse {
    private final long id;
    private final String name;
    private final long leagueId;

    public TeamResponse(final long id, final String name, final long leagueId) {
        this.id = id;
        this.name = name;
        this.leagueId = leagueId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getLeagueId() {
        return leagueId;
    }

    public static TeamResponseBuilder builder() {
        return new TeamResponseBuilder();
    }

    public static final class TeamResponseBuilder {
        private long id;
        private String name;
        private long leagueId;

        public TeamResponseBuilder id(final long id) {
            this.id = id;
            return this;
        }

        public TeamResponseBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public TeamResponseBuilder leagueId(final long leagueId) {
            this.leagueId = leagueId;
            return this;
        }

        public TeamResponse build() {
            return new TeamResponse(
                    this.id,
                    this.name,
                    this.leagueId
            );
        }
    }
}
