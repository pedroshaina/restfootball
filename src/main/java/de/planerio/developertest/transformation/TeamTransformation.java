package de.planerio.developertest.transformation;

import de.planerio.developertest.dto.team.TeamRequest;
import de.planerio.developertest.dto.team.TeamResponse;
import de.planerio.developertest.entity.Team;

import java.util.List;
import java.util.stream.Collectors;

public class TeamTransformation {

    public static Team toEntity(final TeamRequest teamRequest) {
        return new Team(
                teamRequest.getName(),
                teamRequest.getLeagueId()
        );
    }

    public static TeamResponse toResponse(final Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .leagueId(team.getLeagueId())
                .build();
    }

    public static List<TeamResponse> toResponseList(final List<Team> teamList) {
        return teamList.stream()
                .map(TeamTransformation::toResponse)
                .collect(Collectors.toList());
    }
}
