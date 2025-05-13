package pl.football.worldcup;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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
        this.matchStorage = new InMemoryMatchStorage();
        this.matchFactory = new FootballMatchFactory();
        this.scoreBoard = new FootballWorldCupScoreBoard(this.matchStorage, this.matchFactory);
    }

    @Test
    @Order(10)
    void shouldStartMatchSuccessfully() {
        // WHEN
        Long matchId = scoreBoard.startMatch(HOME_TEAM, AWAY_TEAM);

        // THEN
        List<Match> summary = scoreBoard.getSummaryMatchesByTotalScore();
        assertNotNull(matchId);
        assertEquals(1, summary.size());

        Match match = summary.get(0);
        assertEquals(HOME_TEAM, match.homeTeam());
        assertEquals(AWAY_TEAM, match.awayTeam());

        Score score = match.score();
        assertEquals(0, score.homeScore());
        assertEquals(0, score.awayScore());
        assertEquals(0, match.getTotalScore());
    }
}