package de.planerio.developertest.dto.league;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LeagueRequest {

    @NotBlank(message = "`name` is required")
    private String name;

    @NotNull(message = "`countryId` is required")
    private long countryId;


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(final long countryId) {
        this.countryId = countryId;
    }
}
