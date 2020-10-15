package de.planerio.developertest.service;

import de.planerio.developertest.dto.country.CountryLanguage;
import de.planerio.developertest.dto.country.CountryRequest;
import de.planerio.developertest.dto.country.CountryResponse;
import de.planerio.developertest.entity.Country;
import de.planerio.developertest.error.InvalidInputException;
import de.planerio.developertest.error.ResourceNotFoundException;
import de.planerio.developertest.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
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

class CountryServiceTest {

    private final CountryRepository mockCountryRepository = Mockito.mock(CountryRepository.class);
    private final CountryService service = new CountryService(mockCountryRepository);

    @Test
    void givenFindAllWhenValidReturnListOfCountries() {
        //Arrange
        doReturn(createCountryPage()).when(mockCountryRepository).findAll(any(Pageable.class));

        //Act
        final List<CountryResponse> result = service.findAll(null, null);

        //Assert
        assertThat("Result list is empty", result, is(not(empty())));
        assertThat("Result list size does not match", result, hasSize(3));
    }

    @Test
    void givenInvalidCountryIdWhenFindByIdThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockCountryRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The country doesn't exist")));
    }

    @Test
    void givenValidIdWhenFindByIdThenReturnCountry() {
        //Arrange
        doReturn(Optional.of(createCountry(1L, "USA", "en"))).when(mockCountryRepository).findById(anyLong());

        //Act
        final CountryResponse result = service.findById(1L);

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getName(), is(equalTo("USA")));
        assertThat(result.getLanguage(), is(equalTo("en")));
    }

    @Test
    void givenInvalidCountryLanguageWhenSaveThenThrowInvalidInputException() {
        //Arrange

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.save(createCountryRequest("USA", "pt"));
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("%s is an invalid language. The possible values are %s", "pt", Arrays.toString(CountryLanguage.values())))));
    }

    @Test
    void givenValidCountryWhenSaveThenSucceed() {
        //Arrange
        doReturn(createCountry(1L, "USA", "en")).when(mockCountryRepository).save(any(Country.class));

        //Act
        final CountryResponse result = service.save(createCountryRequest("USA", "en"));

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getName(), is(equalTo("USA")));
        assertThat(result.getLanguage(), is(equalTo("en")));
    }

    @Test
    void givenInvalidCountryIdWhenUpdateThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockCountryRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(createCountryRequest("USA", "en"), 1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The country doesn't exist")));
    }

    @Test
    void givenInvalidCountryLanguageWhenUpdateThenThrowInvalidInputException() {
        //Arrange
        doReturn(Optional.of(createCountry(1L, "USA", "en"))).when(mockCountryRepository).findById(anyLong());

        //Act & Assert
        final InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            service.update(createCountryRequest("USA", "pt"), 1L);
        });

        assertThat(exception.getMessage(), is(equalTo(String.format("%s is an invalid language. The possible values are %s", "pt", Arrays.toString(CountryLanguage.values())))));
    }

    @Test
    void givenValidCountryWhenUpdateThenSucceed() {
        //Arrange
        doReturn(Optional.of(createCountry(1L, "USA", "en"))).when(mockCountryRepository).findById(anyLong());
        doReturn(createCountry(1L, "United States", "de")).when(mockCountryRepository).save(any(Country.class));

        //Act
        final CountryResponse result = service.update(createCountryRequest("United States", "de"), 1L);

        //Assert
        assertThat(result.getId(), is(equalTo(1L)));
        assertThat(result.getName(), is(equalTo("United States")));
        assertThat(result.getLanguage(), is(equalTo("de")));
    }

    @Test
    void givenInvalidCountryIdWhenDeleteThenThrowResourceNotFoundException() {
        //Arrange
        doReturn(Optional.empty()).when(mockCountryRepository).findById(anyLong());

        //Act & Assert
        final ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1L);
        });

        assertThat("Exception message does not match", exception.getMessage(), is(equalTo("The country doesn't exist")));
    }

    @Test
    void givenValidCountryIdWhenDeleteThenSucceed() {
        //Arrange
        doReturn(Optional.of(createCountry(1L, "USA", "en"))).when(mockCountryRepository).findById(anyLong());

        //Act
        service.delete(1L);

        //Assert
        verify(mockCountryRepository, times(1)).findById(anyLong());
        verify(mockCountryRepository, times(1)).delete(any(Country.class));
        verifyNoMoreInteractions(mockCountryRepository);
    }

    private Page<Country> createCountryPage() {
        final List<Country> countryList = createCountryList();
        return new PageImpl<>(countryList);
    }

    private List<Country> createCountryList() {
        final List<Country> countries = new ArrayList<>();

        Country created = createCountry(1L, "USA", "en");
        countries.add(created);

        created = createCountry(2L, "Canada", "en");
        countries.add(created);

        created = createCountry(3L, "Germany", "de");
        countries.add(created);

        return countries;
    }

    private Country createCountry(final long id,
                                  final String name,
                                  final String language) {

        final Country country = new Country(name, language);
        country.setId(id);

        return country;
    }

    private CountryRequest createCountryRequest(final String name,
                                                final String language) {
        final CountryRequest request = new CountryRequest();
        request.setName(name);
        request.setLanguage(language);

        return request;
    }
}