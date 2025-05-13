package pl.football.worldcup.storage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.football.worldcup.exception.MatchStorageException;
import pl.football.worldcup.model.FootballMatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryStorageTest {

    private static final String HOME_TEAM = "HomeTeam";
    private static final String AWAY_TEAM = "AwayTeam";

    private MatchStorage matchStorage;
    private Map<Long, FootballMatch> storage;

    @BeforeEach
    void resetState() {
        storage = new HashMap<>();
        matchStorage = new InMemoryStorage(storage);
    }

    @Test
    @Order(10)
    void shouldSaveMatchSuccessfully() {
        // GIVEN
        FootballMatch match = FootballMatch.builder()
                .id(0L)
                .homeTeam(HOME_TEAM)
                .awayTeam(AWAY_TEAM)
                .build();

        // WHEN
        FootballMatch footballMatch = matchStorage.saveMatch(match);

        assertEquals(1, storage.size());
        FootballMatch storedMatch = storage.get(footballMatch.id());
        assertEquals(storedMatch.homeTeam(), footballMatch.homeTeam());
        assertEquals(storedMatch.awayTeam(), footballMatch.awayTeam());
    }

    @Test
    @Order(20)
    void shouldSaveMatchWithExceptionMathIsNull() {
        // WHEN
        MatchStorageException exception = assertThrows(MatchStorageException.class, () -> matchStorage.saveMatch(null));

        // THEN
        assertEquals("Match cannot be null", exception.getMessage());
    }

    @Test
    @Order(30)
    void shouldSaveMatchWithExceptionMatchAlreadyExists() {
        // GIVEN
        FootballMatch match = FootballMatch.builder()
                .id(0L)
                .homeTeam(HOME_TEAM)
                .awayTeam(AWAY_TEAM)
                .build();
        FootballMatch footballMatch = matchStorage.saveMatch(match);

        // WHEN
        MatchStorageException exception = assertThrows(MatchStorageException.class, () -> matchStorage.saveMatch(footballMatch));

        // THEN
        assertEquals("Given match already exist in storage", exception.getMessage());
    }

    @Test
    @Order(40)
    void shouldUpdateMatchSuccessfully() {
        // GIVEN
        FootballMatch match = FootballMatch.builder()
                .id(0L)
                .homeTeam(HOME_TEAM)
                .awayTeam(AWAY_TEAM)
                .build();
        FootballMatch footballMatch = matchStorage.saveMatch(match);
        LocalDateTime startTime = LocalDateTime.now();
        footballMatch = FootballMatch.builder()
                .id(footballMatch.id())
                .homeTeam(footballMatch.homeTeam())
                .awayTeam(footballMatch.awayTeam())
                .startTime(startTime)
                .build();

        // WHEN
        FootballMatch footballMatchUpdated = matchStorage.updateMatch(footballMatch);

        assertEquals(1, storage.size());
        FootballMatch storedMatch = storage.get(footballMatch.id());
        assertEquals(storedMatch.homeTeam(), footballMatchUpdated.homeTeam());
        assertEquals(storedMatch.awayTeam(), footballMatchUpdated.awayTeam());
        assertEquals(storedMatch.startTime(), footballMatchUpdated.startTime());
    }

    @Test
    @Order(50)
    void shouldUpdateMatchWithExceptionMatchIsNull() {
        // WHEN
        MatchStorageException exception = assertThrows(MatchStorageException.class, () -> matchStorage.updateMatch(null));

        // THEN
        assertEquals("Match cannot be null", exception.getMessage());
    }

    @Test
    @Order(60)
    void shouldUpdateMatchWithExceptionMatchNotExists() {
        // GIVEN
        FootballMatch match = FootballMatch.builder()
                .id(1L)
                .homeTeam(HOME_TEAM)
                .awayTeam(AWAY_TEAM)
                .build();

        // WHEN
        MatchStorageException exception = assertThrows(MatchStorageException.class, () -> matchStorage.updateMatch(match));

        // THEN
        assertEquals("Given match not exist in storage", exception.getMessage());
    }

    @Test
    @Order(70)
    void shouldGetMatchSuccessfully() {
        // GIVEN
        FootballMatch match = FootballMatch.builder()
                .id(5L)
                .homeTeam(HOME_TEAM)
                .awayTeam(AWAY_TEAM)
                .build();
        FootballMatch footballMatch = matchStorage.saveMatch(match);

        // WHEN
        FootballMatch storageMatch = matchStorage.getMatch(5L);

        assertEquals(1, storage.size());
        FootballMatch storedMatch = storage.get(footballMatch.id());
        assertEquals(storedMatch.homeTeam(), storageMatch.homeTeam());
        assertEquals(storedMatch.awayTeam(), storageMatch.awayTeam());
        assertEquals(storedMatch.startTime(), storageMatch.startTime());
    }

    @Test
    @Order(80)
    void shouldGetMatchWithExceptionMatchNotExists() {
        // GIVEN
        FootballMatch match = FootballMatch.builder()
                .id(5L)
                .homeTeam(HOME_TEAM)
                .awayTeam(AWAY_TEAM)
                .build();
        matchStorage.saveMatch(match);

        // WHEN
        MatchStorageException exception = assertThrows(MatchStorageException.class, () -> matchStorage.getMatch(4L));

        // THEN
        assertEquals("There is no match with id: 4", exception.getMessage());
    }

    @Test
    @Order(90)
    void shouldGetAllMatchesInProgressSuccessfully() {
        // GIVEN
        FootballMatch match = FootballMatch.builder()
                .id(0L)
                .homeTeam(HOME_TEAM)
                .awayTeam(AWAY_TEAM)
                .startTime(LocalDateTime.now())
                .build();
        FootballMatch footballMatch = matchStorage.saveMatch(match);

        // WHEN
        List<FootballMatch> allMatchesInProgress = matchStorage.getAllMatchesInProgress();

        assertEquals(1, storage.size());
        FootballMatch storedMatch = storage.get(footballMatch.id());
        assertEquals(1, allMatchesInProgress.size());
        assertEquals(storedMatch.homeTeam(), allMatchesInProgress.get(0).homeTeam());
        assertEquals(storedMatch.awayTeam(), allMatchesInProgress.get(0).awayTeam());
        assertEquals(storedMatch.startTime(), allMatchesInProgress.get(0).startTime());
    }

    @Test
    @Order(100)
    void shouldGetAllMatchesInProgressWithMatchesAlreadyFinished() {
        // GIVEN
        FootballMatch match = FootballMatch.builder()
                .id(5L)
                .homeTeam(HOME_TEAM)
                .awayTeam(AWAY_TEAM)
                .startTime(LocalDateTime.now())
                .endTime(Optional.of(LocalDateTime.now()))
                .build();
        FootballMatch footballMatch = matchStorage.saveMatch(match);

        // WHEN
        List<FootballMatch> allMatchesInProgress = matchStorage.getAllMatchesInProgress();

        assertEquals(1, storage.size());
        FootballMatch storedMatch = storage.get(footballMatch.id());
        assertEquals(0, allMatchesInProgress.size());
    }

    @Test
    @Order(110)
    void shouldGetAllMatchesInProgressWithNoMatchesDefined() {
        // WHEN
        List<FootballMatch> allMatchesInProgress = matchStorage.getAllMatchesInProgress();

        assertEquals(0, storage.size());
        assertEquals(0, allMatchesInProgress.size());
    }
}