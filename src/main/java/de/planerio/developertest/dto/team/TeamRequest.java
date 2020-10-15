package de.planerio.developertest.dto.team;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TeamRequest {
    @NotBlank(message = "`name` is required")
    private String name;

    @NotNull(message = "`leagueId` is required")
    private long leagueId;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(final long leagueId) {
        this.leagueId = leagueId;
    }
}
