package pl.football.worldcup;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Builder;

@Builder
record Match(Long id, String homeTeam, String awayTeam, LocalDateTime startTime, Optional<LocalDateTime> endTime, Score score) {

    public static class MatchBuilder {
        private Optional<LocalDateTime> endTime = Optional.empty();
    }

    public Match(Match match, Long id) {
        this(id, match.homeTeam, match.awayTeam, match.startTime, match.endTime, match.score);
    }

    public Match(Match match, Score score) {
        this(match.id, match.homeTeam, match.awayTeam, match.startTime, match.endTime, score);
    }

    public Match(Match match, LocalDateTime endTime) {
        this(match.id, match.homeTeam, match.awayTeam, match.startTime, Optional.of(endTime), match.score);
    }

    public Integer getTotalScore() {
        return score.homeScore() + score.awayScore();
    }
}
