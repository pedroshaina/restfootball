package de.planerio.developertest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApplicationController {

    @Autowired
    private TeamRepository tRepo; // team repository
    @Autowired
    private LeagueRepository lRepo; // league repository
    @Autowired
    private CountryRepository cRepo; // league repository

    /**
     * repository of players
     */
    @Autowired
    private PlayerRepository pRepo;

    /*------------------------------------------------------------------------
     | COUNTRY
     *------------------------------------------------------------------------*/

    @GetMapping("/country")
    public Iterable<Country> getCountries() {
        return cRepo.findAll();
    }

    @PostMapping("/country")
    public Country createTeam(@RequestBody Country c) {
        return cRepo.save(c);
    }

    @PostMapping("/countries")
    public Iterable<Country> createTeams(@RequestBody Iterable<Country> c) {
        return cRepo.saveAll(c);
    }

    @PostMapping("/country/delete/{countryId}")
    public void deleteCountry(@PathVariable long countryId) {
        cRepo.deleteById(countryId);
    }

    @PostMapping("/country/update/{countryId}")
    public void deleteCountry(@RequestBody Country updatedCountry, @PathVariable long countryId) {
        cRepo.findById(countryId).orElseThrow();
        cRepo.save(updatedCountry);
    }

    /*------------------------------------------------------------------------
     | LEAGUE
     *------------------------------------------------------------------------*/

    @PostMapping("/league")
    public League createLeague(@RequestBody League l) {
        return lRepo.save(l);
    }

    @PostMapping("/league/delete/{leagueId}")
    public void deleteLeague(long leagueId) {
        lRepo.deleteById(leagueId);
    }

    /*------------------------------------------------------------------------
     | TEAM
     *------------------------------------------------------------------------*/

    @PostMapping("/team")
    public Team createTeam(@RequestBody Team t) {
        return tRepo.save(t);
    }

    @PostMapping("/team/delete/{teamId}")
    public void deleteTeam(long teamId) {
        tRepo.deleteById(teamId);
    }

    /*------------------------------------------------------------------------
     | PLAYER
     *------------------------------------------------------------------------*/

    @PostMapping("/player")
    public Player createPlayer(@RequestBody Player p) {
        return pRepo.save(p);
    }

    @PostMapping("/player/delete/{playerId}")
    public void deletePlayer(long playerId) {
        pRepo.deleteById(playerId);
    }

}
