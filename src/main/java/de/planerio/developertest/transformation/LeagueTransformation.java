package de.planerio.developertest.transformation;

import de.planerio.developertest.dto.league.LeagueRequest;
import de.planerio.developertest.dto.league.LeagueResponse;
import de.planerio.developertest.entity.League;

import java.util.List;
import java.util.stream.Collectors;

public class LeagueTransformation {
    public static League toEntity(final LeagueRequest leagueRequest) {
        return new League(
                leagueRequest.getName(),
                leagueRequest.getCountryId()
        );
    }

    public static LeagueResponse toResponse(final League league) {
        return LeagueResponse.builder()
                .id(league.getId())
                .name(league.getName())
                .countryId(league.getCountryId())
                .build();
    }

    public static List<LeagueResponse> toResponseList(final List<League> leagueList) {
        return leagueList.stream()
                .map(LeagueTransformation::toResponse)
                .collect(Collectors.toList());
    }
}
