package pl.football.worldcup.storage;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.football.worldcup.exception.MatchStorageException;
import pl.football.worldcup.model.FootballMatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
}