package de.planerio.developertest.rest.country;

import de.planerio.developertest.dto.country.CountryResponse;
import de.planerio.developertest.dto.country.CountryRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.List;


@RequestMapping(
        value = CountryResource.COUNTRY_RESOURCE_PATH,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Country", description = "The country API. (Obs: A country can only have 1 league associated with it!)")
public interface CountryResource {
    String COUNTRY_RESOURCE_PATH = "/country";

    @Operation(
            summary = "Get all countries",
            description = "Retrieve a list of all available countries. (Obs: Results can be paged)"
    )
    @GetMapping
    List<CountryResponse> getAllCountries(
            @RequestParam(name = "page", required = false)
            @Parameter(description = "Current page (starts at 1)") final Integer page,

            @RequestParam(name = "pageSize", required = false)
            @Parameter(description = "Amount of items returned per page") final Integer pageSize
    );

    @Operation(
            summary = "Get specific country by ID",
            description = "Retrieve the given country representation"
    )
    @GetMapping(path = "/{countryId}")
    CountryResponse getCountryById(
            @PathVariable("countryId")
            @Parameter(description = "ID of the country to be retrieved", required = true) final long countryId
    );

    @Operation(
            summary = "Add a country",
            description = "Inserts a country to the database"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    CountryResponse createCountry(
            @Parameter(description = "The country representation", required = true)
            @RequestBody @Valid final CountryRequest country
    );

    @Operation(
            summary = "Update a country",
            description = "Changes the data of a country in the database"
    )
    @PutMapping(path = "/{countryId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    CountryResponse updateCountry(
            @Parameter(description = "The country representation", required = true)
            @RequestBody @Valid final CountryRequest country,

            @Parameter(description = "ID of the country to be modified", required = true)
            @PathVariable("countryId") final long countryId);

    @Operation(
            summary = "Delete a country",
            description = "Removes the country from the database"
    )
    @DeleteMapping(path = "/{countryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCountry(
            @Parameter(description = "ID of the country to be removed")
            @PathVariable("countryId") final long countryId
    );

}
