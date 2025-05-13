package pl.football.worldcup;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import pl.football.worldcup.exception.FootballMatchException;
import pl.football.worldcup.model.FootballMatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class FootballMatchFactoryTest {

    private static final String HOME_TEAM = "HomeTeam";
    private static final String AWAY_TEAM = "AwayTeam";

    private MatchFactory matchFactory;

    @BeforeEach
    void resetState() {
        this.matchFactory = new FootballMatchFactory();
    }

    @Test
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

    @DisplayName("shouldCreateMatchWithExceptionToShortNames:)")
    @ParameterizedTest(name = "case: {0} - {1}")
    @CsvSource(value = {
            "Ab, Cd, Given team names (Ab:Cd) are incorrect. Name should be alphanumeric with minimal length equals 3",
            "Ab, Cdef, Given team name (Ab) is incorrect. Name should be alphanumeric with minimal length equals 3",
            "Abcd, Cd, Given team name (Cd) is incorrect. Name should be alphanumeric with minimal length equals 3",
            "null, null, Given team names (null:null) are incorrect. Name should be alphanumeric with minimal length equals 3",
            "null, Cdef, Given team name (null) is incorrect. Name should be alphanumeric with minimal length equals 3",
            "Abcd, null, Given team name (null) is incorrect. Name should be alphanumeric with minimal length equals 3",

    }, nullValues = "null")
    void shouldCreateMatchWithExceptionToShortNames(String homeTeam, String awayTeam, String expectedException) {
        // GIVEN
        LocalDateTime startTime = LocalDateTime.now();

        // WHEN
        FootballMatchException exception = assertThrows(FootballMatchException.class, () -> matchFactory.createMatch(homeTeam, awayTeam, startTime));

        // THEN
        assertEquals(expectedException, exception.getMessage());
    }


    @DisplayName("shouldCreateMatchWithExceptionStartTimeNull()")
    @ParameterizedTest(name = "case: {0}")
    @NullSource
    void shouldCreateMatchWithExceptionStartTimeNull(LocalDateTime startTime) {
        // WHEN
        FootballMatchException exception =
                assertThrows(FootballMatchException.class, () -> matchFactory.createMatch(HOME_TEAM, AWAY_TEAM, startTime));

        // THEN
        assertEquals("Match startTime cannot be null", exception.getMessage());
    }
}