package de.planerio.developertest.service;

import de.planerio.developertest.dto.country.CountryLanguage;
import de.planerio.developertest.dto.country.CountryRequest;
import de.planerio.developertest.dto.country.CountryResponse;
import de.planerio.developertest.entity.Country;
import de.planerio.developertest.error.InvalidInputException;
import de.planerio.developertest.error.ResourceNotFoundException;
import de.planerio.developertest.repository.CountryRepository;
import de.planerio.developertest.transformation.CountryTransformation;
import de.planerio.developertest.util.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryResponse> findAll(final Integer page, final Integer pageSize) {
        final Page<Country> countries = countryRepository.findAll(PageableUtil.getPageable(page, pageSize));

        return CountryTransformation.toResponseList(countries.getContent());
    }

    public CountryResponse findById(final long countryId) {
        if (countryId <= 0) {
            throw new InvalidInputException("countryId must be greater than 0");
        }

        final Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("The country doesn't exist"));

        return CountryTransformation.toResponse(country);
    }

    public CountryResponse save(final CountryRequest countryRequest) {
        validate(countryRequest);

        final Country entity = CountryTransformation.toEntity(countryRequest);

        return CountryTransformation.toResponse(countryRepository.save(entity));
    }

    public CountryResponse update(final CountryRequest countryRequest, final long countryId) {
        countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("The country doesn't exist"));

        validate(countryRequest);

        final Country country = CountryTransformation.toEntity(countryRequest);
        country.setId(countryId);

        return CountryTransformation.toResponse(countryRepository.save(country));
    }

    public void delete(final long countryId) {
        final Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("The country doesn't exist"));

        countryRepository.delete(country);
    }

    private void validate(final CountryRequest countryRequest) {
        final boolean languageIsValid = Arrays.stream(CountryLanguage.values())
                .anyMatch(language -> language.name().toLowerCase().equals(countryRequest.getLanguage()));

        if (!languageIsValid) {
            throw new InvalidInputException(String.format("%s is an invalid language. The possible values are %s", countryRequest.getLanguage(), Arrays.toString(CountryLanguage.values())));
        }
    }
}
