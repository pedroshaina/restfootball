package de.planerio.developertest.rest.team;

import de.planerio.developertest.dto.team.TeamRequest;
import de.planerio.developertest.dto.team.TeamResponse;

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
        path = TeamResource.TEAM_RESOURCE_PATH,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Team", description = "The team API. (Obs: A team can only have at maximum 25 players associated with it!)")
public interface TeamResource {
    String TEAM_RESOURCE_PATH = "/team";

    @Operation(
            summary = "Get all countries",
            description = "Retrieve a list of all available countries. (Obs: Results can be paged)"
    )
    @GetMapping
    List<TeamResponse> getAllTeams(
            @Parameter(description = "Current page (starts at 1)")
            @RequestParam(name = "page", required = false) final Integer page,

            @Parameter(description = "Amount of items returned per page")
            @RequestParam(name = "pageSize", required = false) final Integer pageSize
    );

    @Operation(
            summary = "Get specific team by ID",
            description = "Retrieve the given team representation"
    )
    @GetMapping(path = "/{teamId}")
    TeamResponse getTeamById(
            @Parameter(description = "ID of the team to be retrieved", required = true)
            @PathVariable("teamId") final long teamId
    );

    @Operation(
            summary = "Add a team",
            description = "Inserts a team to the database"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    TeamResponse createTeam(
            @Parameter(description = "The team representation", required = true)
            @RequestBody @Valid final TeamRequest team
    );

    @Operation(
            summary = "Update a team",
            description = "Changes the data of a team in the database"
    )
    @PutMapping(path = "/{teamId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    TeamResponse updateTeam(
            @Parameter(description = "The team representation", required = true)
            @RequestBody @Valid final TeamRequest team,

            @Parameter(description = "ID of the team to be modified", required = true)
            @PathVariable("teamId") final long teamId
    );

    @Operation(
            summary = "Delete a team",
            description = "Removes the team from the database"
    )
    @DeleteMapping(path = "/{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTeam(
            @Parameter(description = "ID of the team to be removed")
            @PathVariable("teamId") final long teamId
    );

}
