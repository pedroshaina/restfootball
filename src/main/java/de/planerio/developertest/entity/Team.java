package de.planerio.developertest.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "leagueId", insertable = false, updatable = false)
    private League league;

    @Column(nullable = false)
    private long leagueId;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Player> players;

    public Team() {
    }

    public Team(final String name, final long leagueId) {
        this.name = name;
        this.leagueId = leagueId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(final long leagueId) {
        this.leagueId = leagueId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Team team = (Team) o;
        return getId() == team.getId() &&
                getLeagueId() == team.getLeagueId() &&
                getName().equals(team.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLeagueId());
    }
}
