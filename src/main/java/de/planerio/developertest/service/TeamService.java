package de.planerio.developertest.service;

import de.planerio.developertest.dto.team.TeamRequest;
import de.planerio.developertest.dto.team.TeamResponse;
import de.planerio.developertest.entity.League;
import de.planerio.developertest.entity.Team;
import de.planerio.developertest.error.InvalidInputException;
import de.planerio.developertest.error.ResourceNotFoundException;
import de.planerio.developertest.repository.LeagueRepository;
import de.planerio.developertest.repository.TeamRepository;
import de.planerio.developertest.transformation.TeamTransformation;
import de.planerio.developertest.util.PageableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private static final short MAXIMUM_TEAMS_PER_LEAGUE = 20;

    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;

    @Autowired
    public TeamService(final TeamRepository teamRepository,
                       final LeagueRepository leagueRepository) {
        this.teamRepository = teamRepository;
        this.leagueRepository = leagueRepository;
    }

    public List<TeamResponse> findAll(final Integer page, final Integer pageSize) {
        final Page<Team> teams = teamRepository.findAll(PageableUtil.getPageable(page, pageSize));

        return TeamTransformation.toResponseList(teams.getContent());
    }

    public TeamResponse findById(final long teamId) {
        if (teamId <= 0) {
            throw new InvalidInputException("teamId must be greater than 0");
        }

        final Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("The team doesn't exist"));

        return TeamTransformation.toResponse(team);
    }

    public TeamResponse save(final TeamRequest teamRequest) {
        validate(teamRequest);

        final Team entity = TeamTransformation.toEntity(teamRequest);

        return TeamTransformation.toResponse(teamRepository.save(entity));
    }

    public TeamResponse update(final TeamRequest teamRequest, final long teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("The team doesn't exist"));

        final Team team = TeamTransformation.toEntity(teamRequest);
        team.setId(teamId);

        return TeamTransformation.toResponse(teamRepository.save(team));
    }

    public void delete(final long teamId) {
        final Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("The team doesn't exist"));

        teamRepository.delete(team);
    }

    private void validate(final TeamRequest teamRequest) {
        final long leagueId = teamRequest.getLeagueId();

        final League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new InvalidInputException(String.format("The league with id '%d' doesn't exist", leagueId)));

        final int leagueTeamCount = league.getTeams().size();

        if (leagueTeamCount >= MAXIMUM_TEAMS_PER_LEAGUE) {
            throw new InvalidInputException(String.format("The league has %d teams already", MAXIMUM_TEAMS_PER_LEAGUE));
        }
    }
}
