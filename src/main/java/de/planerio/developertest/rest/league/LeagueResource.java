package de.planerio.developertest.rest.league;


import de.planerio.developertest.dto.league.LeagueRequest;
import de.planerio.developertest.dto.league.LeagueResponse;

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
        path = LeagueResource.LEAGUE_RESOURCE_PATH,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "League", description = "The league API. (Obs: A league can only have at maximum 20 teams associated with it!)")
public interface LeagueResource {
    String LEAGUE_RESOURCE_PATH = "/league";

    @Operation(
            summary = "Get all countries",
            description = "Retrieve a list of all available countries. (Obs: Results can be paged)"
    )
    @GetMapping
    List<LeagueResponse> getAllLeagues(
            @Parameter(description = "Country Language used to filter the leagues")
            @RequestParam(name = "countryLanguage", required = false) final String countryLanguage,

            @Parameter(description = "Current page (starts at 1)")
            @RequestParam(name = "page", required = false) final Integer page,

            @Parameter(description = "Amount of items returned per page")
            @RequestParam(name = "pageSize", required = false) final Integer pageSize
    );

    @Operation(
            summary = "Get specific league by ID",
            description = "Retrieve the given league representation"
    )
    @GetMapping(path = "/{leagueId}")
    LeagueResponse getLeagueById(
            @Parameter(description = "ID of the league to be retrieved", required = true)
            @PathVariable("leagueId") final long leagueId
    );

    @Operation(
            summary = "Add a league",
            description = "Inserts a league to the database"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    LeagueResponse createLeague(
            @Parameter(description = "The league representation", required = true)
            @RequestBody @Valid final LeagueRequest league
    );

    @Operation(
            summary = "Update a league",
            description = "Changes the data of a league in the database"
    )
    @PutMapping(path = "/{leagueId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    LeagueResponse updateLeague(
            @Parameter(description = "The league representation", required = true)
            @RequestBody @Valid final LeagueRequest league,

            @Parameter(description = "ID of the league to be modified", required = true)
            @PathVariable("leagueId") final long leagueId
    );

    @Operation(
            summary = "Delete a league",
            description = "Removes the league from the database"
    )
    @DeleteMapping(path = "/{leagueId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLeague(
            @Parameter(description = "ID of the league to be removed")
            @PathVariable("leagueId") final long leagueId
    );

}
