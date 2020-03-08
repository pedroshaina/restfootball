# REST-FOOTBALL #

Welcome, candidates! At this stage of your interview process, you'll need to get your hands (more like fingers) dirty.
The challenge is to upgrade and refactor the API skeleton provided to you in this repository.

The project code contains four entities: Country, League, Team and Player, meant to represent a very simple football
league model. Your job is to achieve the goals listed below to the best of your abilities. Good luck!

### Goals ###

The project code contains four entities: Country, League, Team and Player. For each of these entities, API users should be 
able to:

* Create a new entity;
* Update an existing entity;
* Delete an existing entity;
* Get data for a specific entity;
* List all entities (pagination is optional but appreciated).

### Bonus features ###

The following features are not required, but will make us like you more:

* Listing all leagues per language;
* Listing all players per position;

### Constraints ###

A few constraints will need to be considered when implementing this API:

* A country's language must be one of { "de", "fr", "en", "es", "it" }, in lowercase;
* A player's position must be one of { "GK", "CB", "RB", "LB", "LWB", "RWB", "CDM", "CM", "LM", "RM", "CAM", "ST", "CF" }, in uppercase;
* A team cannot have two players with the same jersey number. Jersery numbers must be in the 1-99 range, inclusive;
* There cannot be more than one league per country;
* There cannot be more than 20 teams per league;
* There cannot be more than 25 players in a team;