package pl.football.worldcup.model;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Builder;

@Builder
public record FootballMatch(Long id, String homeTeam, String awayTeam, LocalDateTime startTime, Optional<LocalDateTime> endTime,
                            MatchScore matchScore) {

    public static class FootballMatchBuilder {
        private Optional<LocalDateTime> endTime = Optional.empty();
    }

    public FootballMatch(FootballMatch match, Long id) {
        this(id, match.homeTeam, match.awayTeam, match.startTime, match.endTime, match.matchScore);
    }

    public FootballMatch(FootballMatch match, MatchScore matchScore) {
        this(match.id, match.homeTeam, match.awayTeam, match.startTime, match.endTime, matchScore);
    }

    public FootballMatch(FootballMatch match, LocalDateTime endTime) {
        this(match.id, match.homeTeam, match.awayTeam, match.startTime, Optional.of(endTime), match.matchScore);
    }

    public Integer getTotalScore() {
        return matchScore.homeScore() + matchScore.awayScore();
    }
}
