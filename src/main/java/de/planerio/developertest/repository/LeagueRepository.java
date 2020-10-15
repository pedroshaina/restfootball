package de.planerio.developertest.repository;

import de.planerio.developertest.entity.League;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface LeagueRepository extends PagingAndSortingRepository<League, Long> {
    List<League> findByCountryLanguage(final String countryLanguage, final Pageable pageable);

    Optional<League> findByCountryId(final long countryId);
}
