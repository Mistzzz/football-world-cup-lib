package pl.football.worldcup;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import pl.football.worldcup.exception.FootballMatchException;

class FootballMatchFactory implements MatchFactory {

    @Override
    public Match createMatch(String homeTeam, String awayTeam, LocalDateTime startTime) {
        validateTeamNames(homeTeam, awayTeam);
        if (startTime == null) {
            throw new FootballMatchException("Match startTime cannot be null");
        }

        return Match.builder()
                .id(0L)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .startTime(startTime)
                .score(initMatchScore())
                .build();
    }

    @Override
    public Match updateMatchScore(Match match, Score score) {
        if (match == null) {
            throw new FootballMatchException("Match cannot be null");
        }
        if (!MatchValidator.isCorrectMatchScores(score)) {
            throw new FootballMatchException("Match score is incorrect. Score should be greater or equal 0");
        }

        return new Match(match, score);
    }

    @Override
    public Match finishMatch(Match match, LocalDateTime endTime) {
        if (match == null) {
            throw new FootballMatchException("Match cannot be null");
        }
        if (endTime == null) {
            throw new FootballMatchException("Match end time cannot be null");
        }
        if (match.endTime().isPresent()) {
            throw new FootballMatchException("Match has been already finished");
        }
        if (MatchValidator.isEndAfterStartTime(match.startTime(), endTime)) {
            return new Match(match, endTime);
        } else {
            throw new FootballMatchException("Match end time value is before start time");
        }
    }

    private void validateTeamNames(String homeTeam, String awayTeam) {
        boolean incorrectHomeTeam = MatchValidator.isIncorrectName(homeTeam);
        boolean incorrectAwayTeam = MatchValidator.isIncorrectName(awayTeam);
        String expectedNameMessage = "Name should be alphanumeric with minimal length equals 3";

        if (incorrectHomeTeam && incorrectAwayTeam) {
            throw new FootballMatchException(
                    MessageFormat.format("Given team names ({0}:{1}) are incorrect. {2}", homeTeam, awayTeam, expectedNameMessage));
        } else {
            String teamName = null;
            if (incorrectHomeTeam) {
                teamName = homeTeam;
            } else if (incorrectAwayTeam) {
                teamName = awayTeam;
            }
            if (incorrectHomeTeam || incorrectAwayTeam) {
                throw new FootballMatchException(MessageFormat.format("Given team name ({0}) is incorrect. {1}", teamName, expectedNameMessage));
            }
        }
    }

    private Score initMatchScore() {
        return new Score(0, 0);
    }
}
