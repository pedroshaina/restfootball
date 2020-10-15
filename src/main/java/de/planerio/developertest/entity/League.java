package de.planerio.developertest.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId", insertable = false, updatable = false)
    private Country country;

    @Column(nullable = false)
    private long countryId;

    @OneToMany(mappedBy = "league", fetch = FetchType.LAZY)
    private List<Team> teams;

    public League() {
    }

    public League(final String name, final long countryId) {
        this.name = name;
        this.countryId = countryId;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(final long countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final League league = (League) o;
        return getId() == league.getId() &&
                getCountryId() == league.getCountryId() &&
                getName().equals(league.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCountryId());
    }
}
