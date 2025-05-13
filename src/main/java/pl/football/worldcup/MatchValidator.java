package pl.football.worldcup;

import java.time.LocalDateTime;

import lombok.experimental.UtilityClass;

@UtilityClass
class MatchValidator {

    private static final String TEAM_NAME_PATTERN = "^\\w{3,}$";

    boolean isIncorrectName(String name) {
        return name == null || !name.matches(TEAM_NAME_PATTERN);
    }

    boolean isCorrectMatchScores(Score scores) {
        return scores != null && scores.homeScore() >= 0 && scores.awayScore() >= 0;
    }

    boolean isEndAfterStartTime(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime != null && endTime != null && endTime.isAfter(startTime);
    }
}
