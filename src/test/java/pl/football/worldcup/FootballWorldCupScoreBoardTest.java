package pl.football.worldcup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;
import pl.football.worldcup.storage.InMemoryStorage;
import pl.football.worldcup.storage.MatchStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FootballWorldCupScoreBoardTest {

    private static final String HOME_TEAM = "HomeTeam";
    private static final String AWAY_TEAM = "AwayTeam";

    private MatchStorage matchStorage;
    private MatchFactory matchFactory;
    private ScoreBoard scoreBoard;

    @BeforeEach
    void resetState() {
        this.matchStorage = new InMemoryStorage();
        this.matchFactory = new FootballMatchFactory();
        this.scoreBoard = new FootballWorldCupScoreBoard(this.matchStorage, this.matchFactory);
    }

    @Test
    @Order(10)
    void shouldStartMatchSuccessfully() {
        // WHEN
        Long matchId = scoreBoard.startMatch(HOME_TEAM, AWAY_TEAM);

        // THEN
        List<FootballMatch> summary = scoreBoard.getSummaryMatchesByTotalScore();
        assertNotNull(matchId);
        assertEquals(1, summary.size());

        FootballMatch match = summary.get(0);
        assertEquals(HOME_TEAM, match.homeTeam());
        assertEquals(AWAY_TEAM, match.awayTeam());

        MatchScore matchScore = match.matchScore();
        assertEquals(0, matchScore.homeScore());
        assertEquals(0, matchScore.awayScore());
        assertEquals(0, match.getTotalScore());
    }
}