package de.planerio.developertest.rest.player;

import de.planerio.developertest.dto.SortingOrder;
import de.planerio.developertest.dto.player.PlayerRequest;
import de.planerio.developertest.dto.player.PlayerResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(
        path = PlayerResource.PLAYER_RESOURCE_PATH,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Player", description = "The player API.")
public interface PlayerResource {
    String PLAYER_RESOURCE_PATH = "/player";

    @Operation(
            summary = "Get all countries",
            description = "Retrieve a list of all available countries. (Obs: Results can be paged)"
    )
    @GetMapping
    List<PlayerResponse> getAllPlayers(
            @Parameter(description = "Position used to filter the players")
            @RequestParam(name = "position", required = false) final String position,

            @Parameter(description = "Current page (starts at 1)")
            @RequestParam(name = "page", required = false) final Integer page,

            @Parameter(description = "Amount of items returned per page")
            @RequestParam(name = "pageSize", required = false) final Integer pageSize
    );

    @Operation(
            summary = "Get all the players who play in a defensive position",
            description = "Retrieve a list of players whose positions are all positions that end in B, plus GK, sorted by the player's last name"
    )
    @GetMapping(path = "/defense")
    List<PlayerResponse> getAllDefensivePlayers(
            @Parameter(description = "The sorting direction (ASC, DESC)")
            @RequestParam(name = "sortingOrder", required = false) final SortingOrder sortingOrder
    );

    @Operation(
            summary = "Get specific player by ID",
            description = "Retrieve the given player representation"
    )
    @GetMapping(path = "/{playerId}")
    PlayerResponse getPlayerById(
            @Parameter(description = "ID of the player to be retrieved", required = true)
            @PathVariable("playerId") final long playerId
    );

    @Operation(
            summary = "Add a player",
            description = "Inserts a player to the database"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    PlayerResponse createPlayer(
            @Parameter(description = "The player representation", required = true)
            @RequestBody @Valid final PlayerRequest player
    );

    @Operation(
            summary = "Update a player",
            description = "Changes the data of a player in the database"
    )
    @PutMapping(path = "/{playerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    PlayerResponse updatePlayer(
            @Parameter(description = "The player representation", required = true)
            @RequestBody @Valid final PlayerRequest player,

            @Parameter(description = "ID of the player to be modified", required = true)
            @PathVariable("playerId") final long playerId
    );

    @Operation(
            summary = "Delete a player",
            description = "Removes the player from the database"
    )
    @DeleteMapping(path = "/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePlayer(
            @Parameter(description = "ID of the player to be removed")
            @PathVariable("playerId") final long playerId
    );

}
