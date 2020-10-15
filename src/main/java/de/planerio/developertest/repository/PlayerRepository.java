package de.planerio.developertest.repository;

import de.planerio.developertest.dto.player.PlayerPosition;
import de.planerio.developertest.entity.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Long> {

    List<Player> findByPosition(final PlayerPosition position, final Pageable pageable);

    List<Player> findByPositionIn(final Set<PlayerPosition> positions, final Sort sort);

    Optional<Player> findByShirtNumberAndTeamId(final int shirtNumber, final long teamId);

}
