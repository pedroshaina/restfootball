package de.planerio.developertest.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import de.planerio.developertest.dto.SortingOrder;
import de.planerio.developertest.dto.player.PlayerPosition;
import de.planerio.developertest.dto.player.PlayerRequest;
import de.planerio.developertest.dto.player.PlayerResponse;
import de.planerio.developertest.entity.Player;
import de.planerio.developertest.entity.Team;
import de.planerio.developertest.error.InvalidInputException;
import de.planerio.developertest.error.ResourceNotFoundException;
import de.planerio.developertest.repository.PlayerRepository;
import de.planerio.developertest.repository.TeamRepository;
import de.planerio.developertest.transformation.PlayerTransformation;
import de.planerio.developertest.util.PageableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PlayerService {

    public static final short MAXIMUM_PLAYERS_PER_TEAM = 25;

    private static final Set<PlayerPosition> DEFENSIVE_POSITIONS = ImmutableSet.of(PlayerPosition.GK,
            PlayerPosition.CB,
            PlayerPosition.RB,
            PlayerPosition.LB,
            PlayerPosition.LWB,
            PlayerPosition.RWB);

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerService(final PlayerRepository playerRepository,
                         final TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public List<PlayerResponse> findAll(final String position, final Integer page, final Integer pageSize) {
        final Pageable pageable = PageableUtil.getPageable(page, pageSize);

        if (!Strings.isNullOrEmpty(position)) {
            return PlayerTransformation.toResponseList(playerRepository.findByPosition(PlayerPosition.valueOf(position), pageable));
        }

        final Page<Player> players = playerRepository.findAll(pageable);

        return PlayerTransformation.toResponseList(players.getContent());
    }

    public List<PlayerResponse> findAllDefensivePlayers(final SortingOrder sortingOrder) {
        final SortingOrder userSorting = Optional.ofNullable(sortingOrder)
                .orElse(SortingOrder.ASC);

        final Sort.Direction direction = Sort.Direction
                .fromOptionalString(userSorting.name())
                .orElse(Sort.Direction.ASC);

        final Sort sort = Sort.by(direction, "lastName");

        return PlayerTransformation.toResponseList(playerRepository.findByPositionIn(DEFENSIVE_POSITIONS, sort));
    }

    public PlayerResponse findById(final long playerId) {
        if (playerId <= 0) {
            throw new InvalidInputException("playerId must be greater than 0");
        }

        final Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("The player doesn't exist"));

        return PlayerTransformation.toResponse(player);
    }

    public PlayerResponse save(final PlayerRequest playerRequest) {
        validate(playerRequest);

        final Player entity = PlayerTransformation.toEntity(playerRequest);

        return PlayerTransformation.toResponse(playerRepository.save(entity));
    }

    public PlayerResponse update(final PlayerRequest playerRequest, final long playerId) {
        playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("The player doesn't exist"));

        validate(playerRequest);

        final Player player = PlayerTransformation.toEntity(playerRequest);
        player.setId(playerId);

        return PlayerTransformation.toResponse(playerRepository.save(player));
    }

    public void delete(final long playerId) {
        final Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("The player doesn't exist"));

        playerRepository.delete(player);
    }

    private void validate(final PlayerRequest playerRequest) {
        validateTeam(playerRequest);
        validatePosition(playerRequest.getPosition());

        playerRepository.findByShirtNumberAndTeamId(playerRequest.getShirtNumber(), playerRequest.getTeamId())
                .ifPresent(existingPlayer -> {
                    throw new InvalidInputException(String.format("The jersey number '%d' already exists in this team", playerRequest.getShirtNumber()));
                });
    }

    private void validateTeam(final PlayerRequest playerRequest) {

        final Team team = teamRepository.findById(playerRequest.getTeamId())
                .orElseThrow(() -> new InvalidInputException(String.format("The team with id '%d' doesn't exist", playerRequest.getTeamId())));

        final int teamPlayerCount = team.getPlayers().size();

        if (teamPlayerCount >= MAXIMUM_PLAYERS_PER_TEAM) {
            throw new InvalidInputException(String.format("The team has %d players already", MAXIMUM_PLAYERS_PER_TEAM));
        }

    }

    private void validatePosition(final String position) {
        if (Arrays.stream(PlayerPosition.values())
                .noneMatch(positionEnum -> positionEnum.name().equals(position))) {

            throw new InvalidInputException(String.format("%s is an invalid position. The possible values are %s", position, Arrays.toString(PlayerPosition.values())));

        }
    }
}
