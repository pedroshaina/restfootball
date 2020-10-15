package de.planerio.developertest.dto.player;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PlayerRequest {

    @NotBlank(message = "`firstName` is required")
    private String firstName;

    @NotBlank(message = "`lastName` is required")
    private String lastName;

    @NotNull(message = "`teamId` is required")
    private long teamId;

    @NotNull(message = "`position` is required")
    private String position;

    @Min(value = 1, message = "`shirtNumber` must be greater or equals 1")
    @Max(value = 99, message = "`shirtNumber` must be less or equals 99")
    private short shirtNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(final long teamId) {
        this.teamId = teamId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(final String position) {
        this.position = position;
    }

    public short getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(final short shirtNumber) {
        this.shirtNumber = shirtNumber;
    }
}
