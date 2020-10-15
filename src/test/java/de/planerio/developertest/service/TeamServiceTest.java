package de.planerio.developertest.service;

import de.planerio.developertest.dto.team.TeamRequest;
import de.planerio.developertest.dto.team.TeamResponse;
import de.planerio.developertest.entity.League;
import de.planerio.developertest.entity.Team;
import de.planerio.developertest.error.InvalidInputException;
import de.planerio.developertest.error.ResourceNotFoundException;
import de.planerio.developertest.repository.LeagueRepository;
import de.planerio.developertest.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class TeamServiceTest {

    private final TeamRepository mockTeamRepository = Mockito.mock(TeamRepository.class);
    private final LeagueRepository mockLeagueRepository = Mockito.mock(LeagueRepository.class);

    private final TeamService service = new TeamService(mockTeamRepository, mockLeagueRepository);

    @Test
    void givenFindAllWhenValidReturnListOfTeams() {
        //Arrange
        doReturn(createTeamPage()).when(mockTeamRepository).findAll(any(Pageable.class));

        //Act
        final List<TeamResponse> result = service.findAll(null,null);

        //Assert
        assertThat("Result list is empty", result, is(not(empty())));
        assertThat("Result list size does not match", result, hasSize(3));
    }

    @Test
    void givenInvalidTeamIdWhenFindByIdThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockTeamRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The team doesn't exist")));
    }

    @Test
    void givenValidIdWhenFindByIdThenReturnTeam() {
        //Arrange
        doReturn(Optional.of(createTeam(1L, "Los Angeles FC", 1L))).when(mockTeamRepository).findById(anyLong());

        //Act
        final TeamResponse result = service.findById(1);

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getName(), is(equalTo("Los Angeles FC")));
        assertThat(result.getLeagueId(), is(equalTo(1L)));
    }

    @Test
    void givenInvalidLeagueWhenSaveThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.empty()).when(mockLeagueRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.save(createTeamRequest("Los Angeles FC", 999L));
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("The league with id '%d' doesn't exist", 999L))));
    }

    @Test
    void givenFullLeagueWhenSaveThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createFullLeague())).when(mockLeagueRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.save(createTeamRequest("Los Angeles FC", 1L));
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo(String.format("The league has %d teams already", TeamService.MAXIMUM_TEAMS_PER_LEAGUE))));
    }

    @Test
    void givenValidTeamWhenSaveThenSucceed() {
        //Arrange
        doReturn(Optional.of(createLeague(1L, "NASL", 1L))).when(mockLeagueRepository).findById(anyLong());
        doReturn(createTeam(1L, "Los Angeles FC", 1L)).when(mockTeamRepository).save(any(Team.class));

        //Act
        final TeamResponse result = service.save(createTeamRequest("Los Angeles FC", 1L));

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getName(), is(equalTo("Los Angeles FC")));
        assertThat(result.getLeagueId(), is(equalTo(1L)));
    }

    @Test
    void givenInvalidTeamIdWhenUpdateThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockTeamRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(createTeamRequest("Los Angeles FC", 1L), 1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The team doesn't exist")));
    }

    @Test
    void givenInvalidLeagueWhenUpdateThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createTeam(1L, "Los Angeles FC", 1L))).when(mockTeamRepository).findById(anyLong());
        doReturn(Optional.empty()).when(mockLeagueRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.update(createTeamRequest("Los Angeles FC", 999L), 1L);
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("The league with id '%d' doesn't exist", 999L))));
    }

    @Test
    void givenFullLeagueWhenUpdateThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createTeam(1L, "Los Angeles FC", 1L))).when(mockTeamRepository).findById(anyLong());
        doReturn(Optional.of(createFullLeague())).when(mockLeagueRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.save(createTeamRequest("Los Angeles FC", 1L));
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo(String.format("The league has %d teams already", TeamService.MAXIMUM_TEAMS_PER_LEAGUE))));
    }

    @Test
    void givenValidTeamWhenUpdateThenSucceed() {
        //Arrange
        doReturn(Optional.of(createTeam(1L, "Los Angeles FC", 1L))).when(mockTeamRepository).findById(anyLong());
        doReturn(Optional.of(createLeague(99L, "NASL", 1L))).when(mockLeagueRepository).findById(anyLong());
        doReturn(createTeam(1L, "Los Angeles FC", 99L)).when(mockTeamRepository).save(any(Team.class));

        //Act
        final TeamResponse result = service.update(createTeamRequest("Los Angeles FC", 99L), 1L);

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getName(), is(equalTo("Los Angeles FC")));
        assertThat(result.getLeagueId(), is(equalTo(99L)));
    }

    @Test
    void givenInvalidTeamIdWhenDeleteThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockTeamRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The team doesn't exist")));
    }

    @Test
    void givenValidTeamIdWhenDeleteThenSucceed() {
        //Arrange
        doReturn(Optional.of(createTeam(1L, "Los Angeles FC", 1L))).when(mockTeamRepository).findById(anyLong());

        //Act
        service.delete(1L);

        //Assert
        verify(mockTeamRepository, times(1)).findById(anyLong());
        verify(mockTeamRepository, times(1)).delete(any(Team.class));
        verifyNoMoreInteractions(mockTeamRepository);
    }

    private Page<Team> createTeamPage() {
        final List<Team> teamList = createTeamList();
        return new PageImpl<>(teamList);
    }

    private List<Team> createTeamList() {
        final List<Team> teams = new ArrayList<>();

        Team created = createTeam(1L, "Los Angeles FC", 1L);
        teams.add(created);

        created = createTeam(2L, "Vancouver Whitecaps FC", 2L);
        teams.add(created);

        created = createTeam(3L, "Bayern München", 3L);
        teams.add(created);

        return teams;
    }

    private Team createTeam(final long id,
                                final String name,
                                final long leagueId) {

        final Team team = new Team(name, leagueId);
        team.setId(id);

        return team;
    }

    private TeamRequest createTeamRequest(final String name,
                                          final long leagueId) {

        final TeamRequest request = new TeamRequest();
        request.setName(name);
        request.setLeagueId(leagueId);

        return request;
    }

    private League createLeague(final long id,
                                final String name,
                                final long countryId) {

        final League league = new League(name, countryId);
        league.setId(id);
        league.setTeams(new ArrayList<>());

        return league;
    }

    private League createFullLeague() {
        final League league = createLeague(1L, "NASL", 1L);
        final List<Team> teams = new ArrayList<>();

        for (long i = 0; i < TeamService.MAXIMUM_TEAMS_PER_LEAGUE; i++) {
            teams.add(
                    createTeam(i + 1L, String.format("Team %d", i + 1L), 1L)
            );
        }

        league.setTeams(teams);

        return league;
    }
}