package de.planerio.developertest.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String language;

    public Country() {

    }

    public Country(final String name, final String language) {
        this.name = name;
        this.language = language;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Country country = (Country) o;
        return getId() == country.getId() &&
                Objects.equals(getName(), country.getName()) &&
                Objects.equals(getLanguage(), country.getLanguage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLanguage());
    }
}
