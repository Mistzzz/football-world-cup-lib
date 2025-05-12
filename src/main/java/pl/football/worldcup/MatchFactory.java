package pl.football.worldcup;

import java.time.LocalDateTime;

import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;

interface MatchFactory {

    FootballMatch createMatch(String homeTeam, String awayTeam, LocalDateTime startTime);

    FootballMatch updateMatchScore(FootballMatch match, MatchScore matchScore);

    FootballMatch finishMatch(FootballMatch match, LocalDateTime endTime);
}
