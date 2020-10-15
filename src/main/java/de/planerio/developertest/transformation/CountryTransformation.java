package de.planerio.developertest.transformation;

import de.planerio.developertest.dto.country.CountryRequest;
import de.planerio.developertest.dto.country.CountryResponse;
import de.planerio.developertest.entity.Country;

import java.util.List;
import java.util.stream.Collectors;

public class CountryTransformation {

    public static Country toEntity(final CountryRequest countryRequest) {
        return new Country(
                countryRequest.getName(),
                countryRequest.getLanguage()
        );
    }

    public static CountryResponse toResponse(final Country country) {
        return CountryResponse.builder()
                .id(country.getId())
                .name(country.getName())
                .language(country.getLanguage())
                .build();
    }

    public static List<CountryResponse> toResponseList(final List<Country> countryList) {
        return countryList.stream()
                .map(CountryTransformation::toResponse)
                .collect(Collectors.toList());
    }
}
