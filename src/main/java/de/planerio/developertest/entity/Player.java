package de.planerio.developertest.entity;

import de.planerio.developertest.dto.player.PlayerPosition;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId", insertable = false, updatable = false)
    private Team team;

    @Column(nullable = false)
    private long teamId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerPosition position;

    @Column(nullable = false)
    private int shirtNumber;

    public Player() {

    }

    public Player(final String firstName,
                  final String lastName,
                  final long teamId,
                  final PlayerPosition position,
                  final int shirtNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamId = teamId;
        this.position = position;
        this.shirtNumber = shirtNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public long getTeamId() { return teamId; }

    public void setTeamId(long teamId) { this.teamId = teamId; }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Player player = (Player) o;
        return getId() == player.getId() &&
                getShirtNumber() == player.getShirtNumber() &&
                getFirstName().equals(player.getFirstName()) &&
                getLastName().equals(player.getFirstName()) &&
                getTeamId() == player.getTeamId() &&
                getPosition().equals(player.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getTeamId(), getPosition(), getShirtNumber());
    }
}
