package de.planerio.developertest.rest.league;

import com.google.common.base.Strings;
import de.planerio.developertest.dto.league.LeagueRequest;
import de.planerio.developertest.dto.league.LeagueResponse;
import de.planerio.developertest.service.LeagueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class LeagueController implements LeagueResource {

    private final LeagueService service;

    @Autowired
    public LeagueController(final LeagueService service) {
        this.service = service;
    }

    @Override
    public List<LeagueResponse> getAllLeagues(final String countryLanguage, final Integer page, final Integer pageSize) {
        return service.findAll(countryLanguage, page, pageSize);
    }

    @Override
    public LeagueResponse getLeagueById(final long leagueId) {
        return service.findById(leagueId);
    }

    @Override
    public LeagueResponse createLeague(final @Valid LeagueRequest league) {
        return service.save(league);
    }

    @Override
    public LeagueResponse updateLeague(final @Valid LeagueRequest league, final long leagueId) {
        return service.update(league, leagueId);
    }

    @Override
    public void deleteLeague(final long leagueId) {
        service.delete(leagueId);
    }
}
