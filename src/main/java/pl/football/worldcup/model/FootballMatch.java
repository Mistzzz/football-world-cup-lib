package pl.football.worldcup.model;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record FootballMatch(Long id, String homeTeam, String awayTeam, LocalDateTime startTime, LocalDateTime endTime, MatchScore score) {

    public Integer getTotalScore() {
        return score.homeScore() + score.awayScore();
    }
}
