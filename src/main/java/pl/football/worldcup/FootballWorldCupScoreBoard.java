package pl.football.worldcup;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import lombok.RequiredArgsConstructor;
import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;

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
        Match match = matchFactory.createMatch(homeTeam, awayTeam, startTime);
        match = matchStorage.saveMatch(match);

        return match.id();
    }

    @Override
    public boolean updateScore(Long id, MatchScore score) {
        try {
            Match match = matchStorage.getMatch(id);
            match = matchFactory.updateMatchScore(match, ModelMapper.map(score));
            matchStorage.updateMatch(match);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public void finishMatch(Long id) {

    }

    @Override
    public List<FootballMatch> getSummaryMatchesByTotalScore() {
        List<Match> allInProgressMatches = matchStorage.getAllMatchesInProgress();
        allInProgressMatches
                .sort(Comparator.comparingInt(Match::getTotalScore)
                        .thenComparing(Match::startTime)
                        .reversed());

        return ModelMapper.map(allInProgressMatches);
    }
}
