package de.planerio.developertest.transformation;

import de.planerio.developertest.dto.player.PlayerPosition;
import de.planerio.developertest.dto.player.PlayerRequest;
import de.planerio.developertest.dto.player.PlayerResponse;
import de.planerio.developertest.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerTransformation {

    public static Player toEntity(final PlayerRequest playerRequest) {
        final PlayerPosition position = PlayerPosition.valueOf(playerRequest.getPosition());
        return new Player(
                playerRequest.getFirstName(),
                playerRequest.getLastName(),
                playerRequest.getTeamId(),
                position,
                playerRequest.getShirtNumber()
        );
    }

    public static PlayerResponse toResponse(final Player player) {
        return PlayerResponse.builder()
                .id(player.getId())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .teamId(player.getTeamId())
                .position(player.getPosition().name())
                .shirtNumber(player.getShirtNumber())
                .build();
    }

    public static List<PlayerResponse> toResponseList(final List<Player> playerList) {
        return playerList.stream()
                .map(PlayerTransformation::toResponse)
                .collect(Collectors.toList());
    }
}
