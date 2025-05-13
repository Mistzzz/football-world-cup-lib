package pl.football.worldcup;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import lombok.RequiredArgsConstructor;
import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;
import pl.football.worldcup.storage.MatchStorage;

@RequiredArgsConstructor
public class FootballWorldCupScoreBoard implements ScoreBoard {

    private final MatchStorage matchStorage;
    private final MatchFactory matchFactory;

    @Override
    public Long startMatch(String homeTeam, String awayTeam) {
        return startMatch(homeTeam, awayTeam, LocalDateTime.now());
    }

    @Override
    public Long startMatch(String homeTeam, String awayTeam, LocalDateTime startTime) {
        FootballMatch match = matchFactory.createMatch(homeTeam, awayTeam, startTime);
        match = matchStorage.saveMatch(match);

        return match.id();
    }

    @Override
    public boolean updateScore(Long id, MatchScore matchScore) {
        return false;
    }

    @Override
    public void finishMatch(Long id) {

    }

    @Override
    public List<FootballMatch> getSummaryMatchesByTotalScore() {
        List<FootballMatch> allInProgressMatches = matchStorage.getAllMatchesInProgress();
        allInProgressMatches
                .sort(Comparator.comparingInt(FootballMatch::getTotalScore)
                        .thenComparing(FootballMatch::startTime)
                        .reversed());

        return allInProgressMatches;
    }
}
