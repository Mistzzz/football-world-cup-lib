package pl.football.worldcup;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import pl.football.worldcup.exception.FootballMatchException;
import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FootballMatchFactoryTest {

    private static final String HOME_TEAM = "HomeTeam";
    private static final String AWAY_TEAM = "AwayTeam";

    private MatchFactory matchFactory;

    @BeforeEach
    void resetState() {
        this.matchFactory = new FootballMatchFactory();
    }

    @Test
    @Order(10)
    void shouldCreateMatchSuccessfully() {
        // GIVEN
        LocalDateTime startTime = LocalDateTime.now();

        // WHEN
        FootballMatch match = matchFactory.createMatch(HOME_TEAM, AWAY_TEAM, startTime);

        assertNotNull(match);
        assertEquals(HOME_TEAM, match.homeTeam());
        assertEquals(AWAY_TEAM, match.awayTeam());
        assertEquals(startTime, match.startTime());
        assertEquals(Optional.empty(), match.endTime());
    }

    @ParameterizedTest(name = "case: {0} - {1}")
    @CsvSource(value = {
            "Ab, Cd, Given team names (Ab:Cd) are incorrect. Name should be alphanumeric with minimal length equals 3",
            "Ab, Cdef, Given team name (Ab) is incorrect. Name should be alphanumeric with minimal length equals 3",
            "Abcd, Cd, Given team name (Cd) is incorrect. Name should be alphanumeric with minimal length equals 3",
            "null, null, Given team names (null:null) are incorrect. Name should be alphanumeric with minimal length equals 3",
            "null, Cdef, Given team name (null) is incorrect. Name should be alphanumeric with minimal length equals 3",
            "Abcd, null, Given team name (null) is incorrect. Name should be alphanumeric with minimal length equals 3",

    }, nullValues = "null")
    @Order(20)
    void shouldCreateMatchWithExceptionToShortNames(String homeTeam, String awayTeam, String expectedException) {
        // GIVEN
        LocalDateTime startTime = LocalDateTime.now();

        // WHEN
        FootballMatchException exception = assertThrows(FootballMatchException.class, () -> matchFactory.createMatch(homeTeam, awayTeam, startTime));

        // THEN
        assertEquals(expectedException, exception.getMessage());
    }


    @ParameterizedTest(name = "case: {0}")
    @NullSource
    @Order(30)
    void shouldCreateMatchWithExceptionStartTimeNull(LocalDateTime startTime) {
        // WHEN
        FootballMatchException exception =
                assertThrows(FootballMatchException.class, () -> matchFactory.createMatch(HOME_TEAM, AWAY_TEAM, startTime));

        // THEN
        assertEquals("Match startTime cannot be null", exception.getMessage());
    }

    @Test
    @Order(40)
    void shouldUpdateMatchSuccessfully() {
        // GIVEN
        int score = 1;
        LocalDateTime startTime = LocalDateTime.now();
        FootballMatch match = matchFactory.createMatch(HOME_TEAM, AWAY_TEAM, startTime);

        // WHEN
        FootballMatch footballMatch = matchFactory.updateMatchScore(match, new MatchScore(score, score));

        // THEN
        assertNotNull(footballMatch);
        assertEquals(HOME_TEAM, footballMatch.homeTeam());
        assertEquals(AWAY_TEAM, footballMatch.awayTeam());
        assertEquals(startTime, footballMatch.startTime());
        assertEquals(score, footballMatch.matchScore().homeScore());
        assertEquals(score, footballMatch.matchScore().awayScore());
    }

    @ParameterizedTest
    @MethodSource("matchScore")
    @Order(50)
    void shouldUpdateMatchWithExceptionMatchScoreIncorrect(MatchScore matchScore) {
        // GIVEN
        LocalDateTime startTime = LocalDateTime.now();
        FootballMatch match = matchFactory.createMatch(HOME_TEAM, AWAY_TEAM, startTime);

        // WHEN
        FootballMatchException exception = assertThrows(FootballMatchException.class, () -> matchFactory.updateMatchScore(match, matchScore));

        // THEN
        assertEquals("Match score is incorrect. Score should be greater or equal 0", exception.getMessage());
    }

    @ParameterizedTest(name = "case: {0}")
    @NullSource
    @Order(60)
    void shouldUpdateMatchWithExceptionMatchNull(FootballMatch match) {
        // GIVEN
        MatchScore matchScore = new MatchScore(1, 0);

        // WHEN
        FootballMatchException exception = assertThrows(FootballMatchException.class, () -> matchFactory.updateMatchScore(match, matchScore));

        // THEN
        assertEquals("Match cannot be null", exception.getMessage());
    }

    @Test
    @Order(70)
    void shouldFinishMatchSuccessfully() {
        // GIVEN
        LocalDateTime startTime = LocalDateTime.now();
        FootballMatch match = matchFactory.createMatch(HOME_TEAM, AWAY_TEAM, startTime);
        LocalDateTime endTime = startTime.plusSeconds(1);

        // WHEN
        match = matchFactory.finishMatch(match, endTime);

        // THEN
        assertNotNull(match);
        assertEquals(HOME_TEAM, match.homeTeam());
        assertEquals(AWAY_TEAM, match.awayTeam());
        assertEquals(startTime, match.startTime());
        assertEquals(endTime, match.endTime());
    }

    @Test
    @Order(80)
    void shouldFinishMatchWithExceptionEndTimeBeforeStartTime() {
        // GIVEN
        LocalDateTime startTime = LocalDateTime.now();
        FootballMatch match = matchFactory.createMatch(HOME_TEAM, AWAY_TEAM, startTime);
        LocalDateTime endTime = startTime.minusSeconds(1);

        // WHEN
        FootballMatchException exception = assertThrows(FootballMatchException.class, () -> matchFactory.finishMatch(match, endTime));

        // THEN
        assertEquals("Match end time value is before start time", exception.getMessage());
    }

    @Test
    @Order(90)
    void shouldFinishMatchWithExceptionMatchAlreadyFinished() {
        // GIVEN
        LocalDateTime startTime = LocalDateTime.now();
        FootballMatch match = matchFactory.createMatch(HOME_TEAM, AWAY_TEAM, startTime);
        LocalDateTime endTime = startTime.plusSeconds(1);
        final FootballMatch savedMatch = matchFactory.finishMatch(match, endTime);

        // WHEN
        FootballMatchException exception = assertThrows(FootballMatchException.class, () -> matchFactory.finishMatch(savedMatch, endTime));

        // THEN
        assertEquals("Match has been already finished", exception.getMessage());
    }

    @ParameterizedTest(name = "case: {0}")
    @NullSource
    @Order(100)
    void shouldFinishMatchWithExceptionMatchNull(FootballMatch match) {
        // GIVEN
        LocalDateTime endTime = LocalDateTime.now();

        // WHEN
        FootballMatchException exception = assertThrows(FootballMatchException.class, () -> matchFactory.finishMatch(match, endTime));

        // THEN
        assertEquals("Match cannot be null", exception.getMessage());
    }

    @ParameterizedTest(name = "case: {0}")
    @NullSource
    @Order(110)
    void shouldFinishMatchWithExceptionEndTimeNull(LocalDateTime endTime) {
        // GIVEN
        FootballMatch match = FootballMatch.builder().build();

        // WHEN
        FootballMatchException exception = assertThrows(FootballMatchException.class, () -> matchFactory.finishMatch(match, endTime));

        // THEN
        assertEquals("Match cannot be null", exception.getMessage());
    }

    private static List<MatchScore> matchScore() {
        return Arrays.asList(
                null,
                new MatchScore(-1, 0),
                new MatchScore(0, -1),
                new MatchScore(-4, -5)
        );
    }
}