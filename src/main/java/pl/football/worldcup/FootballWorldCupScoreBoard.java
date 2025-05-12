package pl.football.worldcup;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;
import pl.football.worldcup.storage.MatchStorage;

@RequiredArgsConstructor
public class FootballWorldCupScoreBoard implements ScoreBoard {

    private final MatchStorage matchStorage;

    @Override
    public Long createMatch(String homeTeam, String awayTeam) {
        return 0L;
    }

    @Override
    public Long createMatch(String homeTeam, String awayTeam, LocalDateTime startTime) {
        return 0L;
    }

    @Override
    public boolean updateMatch(Long id, MatchScore matchScore) {
        return false;
    }

    @Override
    public void finishMatch(Long id) {

    }

    @Override
    public List<FootballMatch> getSummaryMatchesByTotalScore() {
        return List.of();
    }
}
