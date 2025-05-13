package pl.football.worldcup;

import java.time.LocalDateTime;
import java.util.List;

import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;

public interface ScoreBoard {

    Long startMatch(String homeTeam, String awayTeam);

    Long startMatch(String homeTeam, String awayTeam, LocalDateTime startTime);

    boolean updateScore(Long id, MatchScore matchScore);

    void finishMatch(Long id);

    List<FootballMatch> getSummaryMatchesByTotalScore();
}
