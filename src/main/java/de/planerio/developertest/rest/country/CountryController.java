package de.planerio.developertest.rest.country;

import de.planerio.developertest.dto.country.CountryRequest;
import de.planerio.developertest.dto.country.CountryResponse;
import de.planerio.developertest.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CountryController implements CountryResource {

    private final CountryService service;

    @Autowired
    public CountryController(final CountryService service) {
        this.service = service;
    }

    @Override
    public List<CountryResponse> getAllCountries(final Integer page, final Integer pageSize) {
        return service.findAll(page, pageSize);
    }

    @Override
    public CountryResponse getCountryById(final long countryId) {
        return service.findById(countryId);
    }

    @Override
    public CountryResponse createCountry(final @Valid CountryRequest country) {
        return service.save(country);
    }

    @Override
    public CountryResponse updateCountry(final @Valid CountryRequest country, final long countryId) {
        return service.update(country, countryId);
    }

    @Override
    public void deleteCountry(final long countryId) {
        service.delete(countryId);
    }
}
