package pl.football.worldcup;

import java.util.List;

interface MatchStorage {

    Match saveMatch(Match match);

    Match updateMatch(Match match);

    Match getMatch(Long id);

    List<Match> getAllMatchesInProgress();
}
