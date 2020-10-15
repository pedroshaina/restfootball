package de.planerio.developertest.dto.player;

public class PlayerResponse {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final long teamId;
    private final String position;
    private final int shirtNumber;

    public PlayerResponse(final long id,
                          final String firstName,
                          final String lastName,
                          final long teamId,
                          final String position,
                          final int shirtNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamId = teamId;
        this.position = position;
        this.shirtNumber = shirtNumber;
    }

    public static PlayerResponseBuilder builder() {
        return new PlayerResponseBuilder();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getTeamId() {
        return teamId;
    }

    public String getPosition() {
        return position;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public static final class PlayerResponseBuilder {
        private long id;
        private String firstName;
        private String lastName;
        private long teamId;
        private String position;
        private int shirtNumber;

        public PlayerResponseBuilder id(final long id) {
            this.id = id;
            return this;
        }

        public PlayerResponseBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PlayerResponseBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PlayerResponseBuilder teamId(final long teamId) {
            this.teamId = teamId;
            return this;
        }

        public PlayerResponseBuilder position(final String position) {
            this.position = position;
            return this;
        }

        public PlayerResponseBuilder shirtNumber(final int shirtNumber) {
            this.shirtNumber = shirtNumber;
            return this;
        }

        public PlayerResponse build() {
            return new PlayerResponse(
                    this.id,
                    this.firstName,
                    this.lastName,
                    this.teamId,
                    this.position,
                    this.shirtNumber
            );
        }
    }
}
