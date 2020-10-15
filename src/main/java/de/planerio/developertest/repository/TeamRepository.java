package de.planerio.developertest.repository;

import de.planerio.developertest.entity.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {
}
