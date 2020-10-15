package de.planerio.developertest.service;

import de.planerio.developertest.dto.league.LeagueRequest;
import de.planerio.developertest.dto.league.LeagueResponse;
import de.planerio.developertest.entity.Country;
import de.planerio.developertest.entity.League;
import de.planerio.developertest.error.InvalidInputException;
import de.planerio.developertest.error.ResourceNotFoundException;
import de.planerio.developertest.repository.CountryRepository;
import de.planerio.developertest.repository.LeagueRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class LeagueServiceTest {

    private final LeagueRepository mockLeagueRepository = Mockito.mock(LeagueRepository.class);
    private final CountryRepository mockCountryRepository = Mockito.mock(CountryRepository.class);

    private final LeagueService service = new LeagueService(mockLeagueRepository, mockCountryRepository);

    @Test
    void givenFindAllWhenValidReturnListOfLeagues() {
        //Arrange
        doReturn(createLeaguePage()).when(mockLeagueRepository).findAll(any(Pageable.class));

        //Act
        final List<LeagueResponse> result = service.findAll(null,null, null);

        //Assert
        assertThat("Result list is empty", result, is(not(empty())));
        assertThat("Result list size does not match", result, hasSize(3));
    }

    @Test
    void givenFindAllWhenFilterByCountryLanguageThenReturnFilteredListOfLeagues() {
        //Arrange
        doReturn(Collections.singletonList(createLeague(3L, "Bundesliga", 3L))).when(mockLeagueRepository).findByCountryLanguage(anyString(), any(Pageable.class));

        //Act
        final List<LeagueResponse> result = service.findAll("de", null, null);

        //Assert
        assertThat("Result list is empty", result, is(not(empty())));
        assertThat("Result list size does not match", result, hasSize(1));
    }

    @Test
    void givenInvalidLeagueIdWhenFindByIdThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockLeagueRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The league doesn't exist")));
    }

    @Test
    void givenValidIdWhenFindByIdThenReturnLeague() {
        //Arrange
        doReturn(Optional.of(createLeague(1L, "NASL", 1L))).when(mockLeagueRepository).findById(anyLong());

        //Act
        final LeagueResponse result = service.findById(1);

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getName(), is(equalTo("NASL")));
        assertThat(result.getCountryId(), is(equalTo(1L)));
    }

    @Test
    void givenInvalidCountryWhenSaveThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.empty()).when(mockCountryRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.save(createLeagueRequest("NASL", 999L));
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("The country with id '%d' doesn't exist", 999L))));
    }

    @Test
    void givenCountryWithLeagueWhenSaveThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createCountry(1L, "USA", "en"))).when(mockCountryRepository).findById(anyLong());
        doReturn(Optional.of(createLeague(1L, "NASL", 1L))).when(mockLeagueRepository).findByCountryId(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.save(createLeagueRequest("NASL", 1L));
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("There is already one league in this country")));
    }

    @Test
    void givenValidLeagueWhenSaveThenSucceed() {
        //Arrange
        doReturn(Optional.of(createCountry(1L, "USA", "en"))).when(mockCountryRepository).findById(anyLong());
        doReturn(createLeague(1L, "NASL", 1L)).when(mockLeagueRepository).save(any(League.class));

        //Act
        final LeagueResponse result = service.save(createLeagueRequest("NASL", 1L));

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getName(), is(equalTo("NASL")));
        assertThat(result.getCountryId(), is(equalTo(1L)));
    }

    @Test
    void givenInvalidLeagueIdWhenUpdateThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockLeagueRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(createLeagueRequest("NASL", 1L), 1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The league doesn't exist")));
    }

    @Test
    void givenInvalidCountryWhenUpdateThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createLeague(1L, "NASL", 1L))).when(mockLeagueRepository).findById(anyLong());
        doReturn(Optional.empty()).when(mockCountryRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.update(createLeagueRequest("NASL", 999L), 1L);
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("The country with id '%d' doesn't exist", 999L))));
    }

    @Test
    void givenCountryWithLeagueWhenUpdateThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createLeague(1L, "NASL", 1L))).when(mockLeagueRepository).findById(anyLong());
        doReturn(Optional.of(createCountry(999L, "USA", "en"))).when(mockCountryRepository).findById(anyLong());
        doReturn(Optional.of(createLeague(1L, "NASL", 1L))).when(mockLeagueRepository).findByCountryId(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.update(createLeagueRequest("NASL", 999L), 1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("There is already one league in this country")));
    }

    @Test
    void givenValidLeagueWhenUpdateThenSucceed() {
        //Arrange
        doReturn(Optional.of(createLeague(1L, "NASL", 1L))).when(mockLeagueRepository).findById(anyLong());
        doReturn(Optional.of(createCountry(99L, "USA", "en"))).when(mockCountryRepository).findById(anyLong());
        doReturn(Optional.empty()).when(mockLeagueRepository).findByCountryId(anyLong());
        doReturn(createLeague(1L, "NASL", 99L)).when(mockLeagueRepository).save(any(League.class));

        //Act
        final LeagueResponse result = service.update(createLeagueRequest("NASL", 99L), 1L);

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getName(), is(equalTo("NASL")));
        assertThat(result.getCountryId(), is(equalTo(99L)));
    }

    @Test
    void givenInvalidLeagueIdWhenDeleteThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockLeagueRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The league doesn't exist")));
    }

    @Test
    void givenValidLeagueIdWhenDeleteThenSucceed() {
        //Arrange
        doReturn(Optional.of(createLeague(1L, "USA", 1L))).when(mockLeagueRepository).findById(anyLong());

        //Act
        service.delete(1L);

        //Assert
        verify(mockLeagueRepository, times(1)).findById(anyLong());
        verify(mockLeagueRepository, times(1)).delete(any(League.class));
        verifyNoMoreInteractions(mockLeagueRepository);
    }

    private Page<League> createLeaguePage() {
        final List<League> leagueList = createLeagueList();
        return new PageImpl<>(leagueList);
    }

    private List<League> createLeagueList() {
        final List<League> leagues = new ArrayList<>();

        League created = createLeague(1L, "NASL", 1L);
        leagues.add(created);

        created = createLeague(2L, "Canadian Soccer League", 2L);
        leagues.add(created);

        created = createLeague(3L, "Bundesliga", 3L);
        leagues.add(created);

        return leagues;
    }

    private League createLeague(final long id,
                                final String name,
                                final long countryId) {

        final League league = new League(name, countryId);
        league.setId(id);

        return league;
    }

    private LeagueRequest createLeagueRequest(final String name,
                                              final long countryId) {
        final LeagueRequest request = new LeagueRequest();
        request.setName(name);
        request.setCountryId(countryId);

        return request;
    }

    private Country createCountry(final long id,
                                  final String name,
                                  final String language) {

        final Country country = new Country(name, language);
        country.setId(id);

        return country;
    }
}