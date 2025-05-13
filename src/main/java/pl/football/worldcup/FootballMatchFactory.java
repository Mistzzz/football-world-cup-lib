package pl.football.worldcup;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import pl.football.worldcup.exception.FootballMatchException;
import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;
import pl.football.worldcup.util.MatchValidator;

class FootballMatchFactory implements MatchFactory {

    @Override
    public FootballMatch createMatch(String homeTeam, String awayTeam, LocalDateTime startTime) {
        validateTeamNames(homeTeam, awayTeam);
        if (startTime == null) {
            throw new FootballMatchException("Match startTime cannot be null");
        }

        return FootballMatch.builder()
                .id(0L)
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .startTime(startTime)
                .matchScore(initMatchScore())
                .build();
    }

    @Override
    public FootballMatch updateMatchScore(FootballMatch match, MatchScore matchScore) {
        return null;
    }

    @Override
    public FootballMatch finishMatch(FootballMatch match, LocalDateTime endTime) {
        return null;
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

    private MatchScore initMatchScore() {
        return new MatchScore(0, 0);
    }
}
