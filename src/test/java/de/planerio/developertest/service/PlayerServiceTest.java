package de.planerio.developertest.service;

import de.planerio.developertest.dto.player.PlayerPosition;
import de.planerio.developertest.dto.player.PlayerRequest;
import de.planerio.developertest.dto.player.PlayerResponse;
import de.planerio.developertest.entity.Player;
import de.planerio.developertest.entity.Team;
import de.planerio.developertest.error.InvalidInputException;
import de.planerio.developertest.error.ResourceNotFoundException;
import de.planerio.developertest.repository.PlayerRepository;
import de.planerio.developertest.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

class PlayerServiceTest {

    private final PlayerRepository mockPlayerRepository = Mockito.mock(PlayerRepository.class);
    private final TeamRepository mockTeamRepository = Mockito.mock(TeamRepository.class);

    private final PlayerService service = new PlayerService(mockPlayerRepository, mockTeamRepository);

    @Test
    void givenFindAllWhenValidReturnListOfPlayers() {
        //Arrange
        doReturn(createPlayerPage()).when(mockPlayerRepository).findAll(any(Pageable.class));

        //Act
        final List<PlayerResponse> result = service.findAll(null,null,null);

        //Assert
        assertThat("Result list is empty", result, is(not(empty())));
        assertThat("Result list size does not match", result, hasSize(3));
    }

    @Test
    void givenFindAllWhenFilterByPositionThenReturnFilteredListOfLeagues() {
        //Arrange
        doReturn(Collections.singletonList(createDefaultPlayer())).when(mockPlayerRepository).findByPosition(any(PlayerPosition.class), any(Pageable.class));

        //Act
        final List<PlayerResponse> result = service.findAll("ST", null, null);

        //Assert
        assertThat("Result list is empty", result, is(not(empty())));
        assertThat("Result list size does not match", result, hasSize(1));
    }

    @Test
    void givenInvalidPlayerIdWhenFindByIdThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockPlayerRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The player doesn't exist")));
    }

    @Test
    void givenValidIdWhenFindByIdThenReturnPlayer() {
        //Arrange
        doReturn(Optional.of(createDefaultPlayer())).when(mockPlayerRepository).findById(anyLong());

        //Act
        final PlayerResponse result = service.findById(1);

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getFirstName(), is(equalTo("Pedro")));
        assertThat(result.getLastName(), is(equalTo("Martins")));
        assertThat(result.getTeamId(), is(equalTo(1L)));
        assertThat(result.getPosition(), is(equalTo(PlayerPosition.ST.name())));
        assertThat(result.getShirtNumber(), is(equalTo(99)));
    }

    @Test
    void givenInvalidTeamWhenSaveThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.empty()).when(mockTeamRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.save(createPlayerRequest( "Pedro", "Martins", 999L, "ST", (short) 99));
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("The team with id '%d' doesn't exist", 999L))));
    }

    @Test
    void givenInvalidPositionWhenSaveThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createTeam(1L, "Los Angeles FC", 1L))).when(mockTeamRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.save(createPlayerRequest( "Pedro", "Martins", 1L, "INEXISTENT", (short) 99));
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("%s is an invalid position. The possible values are %s", "INEXISTENT", Arrays.toString(PlayerPosition.values())))));
    }

    @Test
    void givenFullTeamWhenSaveThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createFullTeam())).when(mockTeamRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.save(createPlayerRequest( "Pedro", "Martins", 1L, "ST", (short) 99));
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo(String.format("The team has %d players already", PlayerService.MAXIMUM_PLAYERS_PER_TEAM))));
    }

    @Test
    void givenValidPlayerWhenSaveThenSucceed() {
        //Arrange
        doReturn(Optional.of(createTeam(1L, "Los Angeles FC", 1L))).when(mockTeamRepository).findById(anyLong());
        doReturn(createDefaultPlayer()).when(mockPlayerRepository).save(any(Player.class));

        //Act
        final PlayerResponse result = service.save(createPlayerRequest( "Pedro", "Martins", 1L, "ST", (short) 99));

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getFirstName(), is(equalTo("Pedro")));
        assertThat(result.getLastName(), is(equalTo("Martins")));
        assertThat(result.getTeamId(), is(equalTo(1L)));
        assertThat(result.getPosition(), is(equalTo(PlayerPosition.ST.name())));
        assertThat(result.getShirtNumber(), is(equalTo(99)));
    }

    @Test
    void givenInvalidTeamWhenUpdateThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createDefaultPlayer())).when(mockPlayerRepository).findById(anyLong());
        doReturn(Optional.empty()).when(mockTeamRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.update(createPlayerRequest( "Pedro", "Martins", 999L, "ST", (short) 99), 1L);
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("The team with id '%d' doesn't exist", 999L))));
    }

    @Test
    void givenInvalidPositionWhenUpdateThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createDefaultPlayer())).when(mockPlayerRepository).findById(anyLong());
        doReturn(Optional.of(createTeam(1L, "Los Angeles FC", 1L))).when(mockTeamRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.update(createPlayerRequest( "Pedro", "Martins", 1L, "INEXISTENT", (short) 99), 1L);
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("%s is an invalid position. The possible values are %s", "INEXISTENT", Arrays.toString(PlayerPosition.values())))));
    }

    @Test
    void givenFullTeamWhenUpdateThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createDefaultPlayer())).when(mockPlayerRepository).findById(anyLong());
        doReturn(Optional.of(createFullTeam())).when(mockTeamRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.update(createPlayerRequest( "Pedro", "Martins", 1L, "ST", (short) 99), 1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo(String.format("The team has %d players already", PlayerService.MAXIMUM_PLAYERS_PER_TEAM))));
    }

    @Test
    void givenValidPlayerWhenUpdateThenSucceed() {
        //Arrange
        doReturn(Optional.of(createDefaultPlayer())).when(mockPlayerRepository).findById(anyLong());
        doReturn(Optional.of(createTeam(1L, "Los Angeles FC", 1L))).when(mockTeamRepository).findById(anyLong());
        doReturn(createPlayer(1L, "Pedro", "Martins", 1L, "ST", 98)).when(mockPlayerRepository).save(any(Player.class));

        //Act
        final PlayerResponse result = service.update(createPlayerRequest( "Pedro", "Martins", 1L, "ST", (short) 98), 1L);

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getFirstName(), is(equalTo("Pedro")));
        assertThat(result.getLastName(), is(equalTo("Martins")));
        assertThat(result.getTeamId(), is(equalTo(1L)));
        assertThat(result.getPosition(), is(equalTo(PlayerPosition.ST.name())));
        assertThat(result.getShirtNumber(), is(equalTo(98)));
    }

    @Test
    void givenInvalidPlayerIdWhenDeleteThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockPlayerRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The player doesn't exist")));
    }

    @Test
    void givenValidPlayerIdWhenDeleteThenSucceed() {
        //Arrange
        doReturn(Optional.of(createDefaultPlayer())).when(mockPlayerRepository).findById(anyLong());

        //Act
        service.delete(1L);

        //Assert
        verify(mockPlayerRepository, times(1)).findById(anyLong());
        verify(mockPlayerRepository, times(1)).delete(any(Player.class));
        verifyNoMoreInteractions(mockPlayerRepository);
    }

    private Page<Player> createPlayerPage() {
        final List<Player> playerList = createPlayerList();
        return new PageImpl<>(playerList);
    }

    private List<Player> createPlayerList() {
        final List<Player> players = new ArrayList<>();

        Player created = createDefaultPlayer();
        players.add(created);

        created = createPlayer(1L, "Pedro", "Martins RWB", 1L, "RWB", 98);
        players.add(created);

        created = createPlayer(1L, "Pedro", "Martins GK", 1L, "GK", 97);
        players.add(created);

        return players;
    }

    private Player createDefaultPlayer() {
        return createPlayer(1L, "Pedro", "Martins", 1L, "ST", 99);
    }

    private Player createPlayer(final long id,
                                final String firstName,
                                final String lastName,
                                final long teamId,
                                final String position,
                                final int shirtNumber) {

        final PlayerPosition playerPosition = PlayerPosition.valueOf(position);

        final Player player = new Player(firstName, lastName, teamId, playerPosition, shirtNumber);
        player.setId(id);

        return player;
    }

    private PlayerRequest createPlayerRequest(final String firstName,
                                              final String lastName,
                                              final long teamId,
                                              final String position,
                                              final short shirtNumber) {

        final PlayerRequest request = new PlayerRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setTeamId(teamId);
        request.setPosition(position);
        request.setShirtNumber(shirtNumber);

        return request;
    }

    private Team createTeam(final long id,
                            final String name,
                            final long leagueId) {

        final Team team = new Team(name, leagueId);
        team.setId(id);
        team.setPlayers(new ArrayList<>());

        return team;
    }

    private Team createFullTeam() {
        final Team team = createTeam(1L, "Los Angeles FC", 1L);
        final List<Player> players = new ArrayList<>();

        for (long i = 0; i < PlayerService.MAXIMUM_PLAYERS_PER_TEAM; i++) {
            players.add(
                    createPlayer(i + 1L, "Player", String.format("%d", i + 1L), 1L, "ST", 99 - Long.valueOf(i).intValue())
            );
        }

        team.setPlayers(players);

        return team;
    }
}