package de.planerio.developertest.service;

import com.google.common.base.Strings;
import de.planerio.developertest.dto.league.LeagueRequest;
import de.planerio.developertest.dto.league.LeagueResponse;
import de.planerio.developertest.entity.League;
import de.planerio.developertest.error.InvalidInputException;
import de.planerio.developertest.error.ResourceNotFoundException;
import de.planerio.developertest.repository.CountryRepository;
import de.planerio.developertest.repository.LeagueRepository;
import de.planerio.developertest.transformation.LeagueTransformation;
import de.planerio.developertest.util.PageableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public LeagueService(final LeagueRepository leagueRepository,
                         final CountryRepository countryRepository) {
        this.leagueRepository = leagueRepository;
        this.countryRepository = countryRepository;
    }

    public List<LeagueResponse> findAll(final String countryLanguage, final Integer page, final Integer pageSize) {
        final Pageable pageable = PageableUtil.getPageable(page, pageSize);

        if (!Strings.isNullOrEmpty(countryLanguage)) {
            return LeagueTransformation.toResponseList(leagueRepository.findByCountryLanguage(countryLanguage, pageable));
        }

        final Page<League> leagues = leagueRepository.findAll(pageable);

        return LeagueTransformation.toResponseList(leagues.getContent());
    }

    public LeagueResponse findById(final long leagueId) {
        if (leagueId <= 0) {
            throw new InvalidInputException("leagueId must be greater than 0");
        }

        final League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new ResourceNotFoundException("The league doesn't exist"));

        return LeagueTransformation.toResponse(league);
    }

    public LeagueResponse save(final LeagueRequest leagueRequest) {
        validate(leagueRequest);

        final League entity = LeagueTransformation.toEntity(leagueRequest);

        return LeagueTransformation.toResponse(leagueRepository.save(entity));
    }

    public LeagueResponse update(final LeagueRequest leagueRequest, final long leagueId) {
        leagueRepository.findById(leagueId)
                .orElseThrow(() -> new ResourceNotFoundException("The league doesn't exist"));

        final League league = LeagueTransformation.toEntity(leagueRequest);
        league.setId(leagueId);

        return LeagueTransformation.toResponse(leagueRepository.save(league));
    }

    public void delete(final long leagueId) {
        final League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new ResourceNotFoundException("The league doesn't exist"));

        leagueRepository.delete(league);
    }

    private void validate(final LeagueRequest leagueRequest) {
        final long countryId = leagueRequest.getCountryId();

        if (countryId <= 0) {
            throw new InvalidInputException("countryId must be greater than 0");
        }

        countryRepository.findById(countryId)
                .orElseThrow(() -> new InvalidInputException(String.format("The country with id '%d' doesn't exist", countryId)));

        leagueRepository.findByCountryId(countryId)
                .ifPresent(existingLeague -> {
                    throw new InvalidInputException("There is already one league in this country");
                });
    }

}
