package de.planerio.developertest.rest.player;

import de.planerio.developertest.dto.SortingOrder;
import de.planerio.developertest.dto.player.PlayerRequest;
import de.planerio.developertest.dto.player.PlayerResponse;
import de.planerio.developertest.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PlayerController implements PlayerResource {

    private final PlayerService service;

    @Autowired
    public PlayerController(final PlayerService service) {
        this.service = service;
    }

    @Override
    public List<PlayerResponse> getAllPlayers(final String position, final Integer page, final Integer pageSize) {
        return service.findAll(position, page, pageSize);
    }

    @Override
    public List<PlayerResponse> getAllDefensivePlayers(final SortingOrder sortingOrder) {
        return service.findAllDefensivePlayers(sortingOrder);
    }

    @Override
    public PlayerResponse getPlayerById(final long playerId) {
        return service.findById(playerId);
    }

    @Override
    public PlayerResponse createPlayer(final @Valid PlayerRequest player) {
        return service.save(player);
    }

    @Override
    public PlayerResponse updatePlayer(final @Valid PlayerRequest player, final long playerId) {
        return service.update(player, playerId);
    }

    @Override
    public void deletePlayer(final long playerId) {
        service.delete(playerId);
    }

}
