package pl.football.worldcup.model;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Builder;

@Builder
public record FootballMatch(Long id, String homeTeam, String awayTeam, LocalDateTime startTime, Optional<LocalDateTime> endTime,
                            MatchScore matchScore) {

}
