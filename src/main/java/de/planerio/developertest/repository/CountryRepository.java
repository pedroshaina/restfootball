package de.planerio.developertest.repository;

import de.planerio.developertest.entity.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CountryRepository extends PagingAndSortingRepository<Country, Long> {
    //List<Country> findAll(final Pageable pageable);
}
