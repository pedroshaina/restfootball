package de.planerio.developertest.rest.team;

import de.planerio.developertest.dto.team.TeamRequest;
import de.planerio.developertest.dto.team.TeamResponse;
import de.planerio.developertest.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TeamController implements TeamResource {

    private final TeamService service;

    @Autowired
    public TeamController(final TeamService service) {
        this.service = service;
    }

    @Override
    public List<TeamResponse> getAllTeams(final Integer page, final Integer pageSize) {
        return service.findAll(page, pageSize);
    }

    @Override
    public TeamResponse getTeamById(final long teamId) {
        return service.findById(teamId);
    }

    @Override
    public TeamResponse createTeam(final @Valid TeamRequest team) {
        return service.save(team);
    }

    @Override
    public TeamResponse updateTeam(final @Valid TeamRequest team, final long teamId) {
        return service.update(team, teamId);
    }

    @Override
    public void deleteTeam(final long teamId) {
        service.delete(teamId);
    }
}
