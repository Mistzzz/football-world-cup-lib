package pl.football.worldcup;

import java.time.LocalDateTime;
import java.util.List;

public interface ScoreBoard {

    Long startMatch(String homeTeam, String awayTeam);

    Long startMatch(String homeTeam, String awayTeam, LocalDateTime startTime);

    boolean updateScore(Long id, Score score);

    void finishMatch(Long id);

    List<Match> getSummaryMatchesByTotalScore();
}
