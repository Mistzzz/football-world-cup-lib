package pl.football.worldcup;

import java.time.LocalDateTime;

interface MatchFactory {

    Match createMatch(String homeTeam, String awayTeam, LocalDateTime startTime);

    Match updateMatchScore(Match match, Score score);

    Match finishMatch(Match match, LocalDateTime endTime);
}
