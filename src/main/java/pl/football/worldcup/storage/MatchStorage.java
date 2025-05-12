package pl.football.worldcup.storage;

import java.util.List;

import pl.football.worldcup.model.FootballMatch;

public interface MatchStorage {

    FootballMatch saveMatch(FootballMatch match);

    FootballMatch updateMatch(FootballMatch match);

    FootballMatch getMatch(Long id);

    List<FootballMatch> getAllMatchesInProgress();
}
